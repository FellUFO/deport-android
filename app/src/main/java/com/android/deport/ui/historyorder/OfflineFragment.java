package com.android.deport.ui.historyorder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.deport.R;
import com.android.deport.data.DeportRoomDatabase;
import com.android.deport.data.adapters.DocumentAdapter;
import com.android.deport.data.dao.DocumentMasterDao;
import com.android.deport.data.dao.DocumentSlaveDao;
import com.android.deport.data.entity.DocumentMaster;
import com.android.deport.data.entity.DocumentSlave;
import com.android.deport.data.entity.MasterAndSlave;
import com.android.deport.data.entity.Unit;
import com.android.deport.ui.MainActivity;
import com.android.deport.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 显示本机的离线单据
 */
public class OfflineFragment extends Fragment implements View.OnClickListener {

    private DocumentAdapter mAdapter;
    private RecyclerView recycler;
    private List<Unit<DocumentMaster, DocumentSlave>> date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 对RecyclerView进行初始化
     *
     * @param date
     */
    private void initRecyclerView(List<Unit<DocumentMaster, DocumentSlave>> date) {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DocumentAdapter(getActivity(), R.layout.task_master_list, R.layout.task_slave_list, date);
        recycler.setAdapter(mAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.offline_history_order, container, false);
        recycler = root.findViewById(R.id.recycler_offline_order);
        getDate();
        Button upload = root.findViewById(R.id.upload);
        SwipeRefreshLayout mRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.layout_swipe_refresh);
        mRefreshLayout.setOnRefreshListener( () -> {
                getDate();
                mRefreshLayout.setRefreshing(false);
                Message message = new Message();
                message.obj = "数据已刷新";
                secondHandler.sendMessage(message);
        });

        upload.setOnClickListener(this);
        return root;
    }

    void clear() {
        new Thread( () -> {
            DeportRoomDatabase dataBase = DeportRoomDatabase.getDataBase(getContext());
            DocumentMasterDao documentMasterDao = dataBase.documentMasterDao();
            DocumentSlaveDao documentSlaveDao = dataBase.documentSlaveDao();
            documentMasterDao.deleteAll();
            documentSlaveDao.deleteAll();
        }).start();
    }

    /**
     * 获取数据
     *
     * @return
     */
    private void getDate() {
        List<Unit<DocumentMaster, DocumentSlave>> units = new ArrayList<>();
        new Thread( () -> {
            DeportRoomDatabase db = DeportRoomDatabase.getDataBase(getContext());
            DocumentMasterDao dmDao = db.documentMasterDao();
            DocumentSlaveDao dsDao = db.documentSlaveDao();
            List<DocumentMaster> masters = dmDao.getDocumentByNotUploaded();
            for (DocumentMaster master : masters) {
                Log.d("master", master.toString());
            }
            if (masters != null) {
                for (DocumentMaster master : masters) {
                    List<DocumentSlave> slaves = dsDao.getDocumentByMasterId(master.getOrderId());
                    units.add(new Unit<DocumentMaster, DocumentSlave>(master, slaves));
                }
                    Message msg = new Message();
                    msg.obj = units;
                    firstHandler.sendMessage(msg);
                }
        }).start();
    }

private Handler firstHandler = new Handler() {
    @Override
    public void handleMessage(@NonNull Message msg) {
        date = (List<Unit<DocumentMaster, DocumentSlave>>) msg.obj;
        initRecyclerView(date);
    }
};

    Handler secondHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            ToastUtil.getShortToast(msg.obj.toString(), getContext());
        }
    };



    @Override
    public void onClick(View view) {
        if (MainActivity.isInternet) {
            if (view.getId() == R.id.upload) {
//                if (date.isEmpty()) {
                    //发送请求，传输数据
                    new Thread( () -> {
                            List<Unit<DocumentMaster, DocumentSlave>> units = new ArrayList<>();
                            DeportRoomDatabase db = DeportRoomDatabase.getDataBase(getContext());
                            DocumentMasterDao dmDao = db.documentMasterDao();
                            DocumentSlaveDao dsDao = db.documentSlaveDao();
                            List<DocumentMaster> masters = dmDao.getDocumentByNotUploaded();
                            for (DocumentMaster master : masters) {
                                Log.d("ssmaster", master.toString());
                            }
                            if (masters != null) {
                                for (DocumentMaster master : masters) {
                                    List<DocumentSlave> slaves = dsDao.getDocumentByMasterId(master.getOrderId());
                                    units.add(new Unit<DocumentMaster, DocumentSlave>(master, slaves));
                                }
                            }
                            List<MasterAndSlave> masterAndSlaves = new ArrayList<>();
                            for (Unit<DocumentMaster, DocumentSlave> unit : units) {
                                masterAndSlaves.add(new MasterAndSlave(unit.group, unit.children));
                            }
                            OkHttpClient client = new OkHttpClient();
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            Gson gson = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .create();
                            String content ="";
                            if (!masterAndSlaves.isEmpty()) {
                                content = gson.toJson(masterAndSlaves);
                                Log.i("documentMasterJSON", content);
                            } else {
                                Log.i("documentMasterJSON", "documentMasterJSON");
                            }
                            RequestBody body = RequestBody.create(JSON, content);
                            String url = "http://192.168.0.116:8080/createListDocument";
                            Request request = new Request.Builder()
                                    .post(body)
                                    .url(url)
                                    .build();
                            Call call = client.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String responseBody = Objects.requireNonNull(response.body()).string();
                                    if (responseBody.contains("成功")) {
                                        DeportRoomDatabase db = DeportRoomDatabase.getDataBase(getContext());
                                        DocumentMasterDao masterDao = db.documentMasterDao();
                                        for (Unit<DocumentMaster, DocumentSlave> unit : date) {
                                            //改变单据状态
                                            masterDao.updateDocumentByState(unit.group.getOrderId());
                                            Log.d("OrderId", unit.group.getOrderId());
                                        }
                                        Message message = new Message();
                                        message.obj = responseBody;
                                        secondHandler.sendMessage(message);
                                    }
                                }
                            });
                    }).start();
            }
        } else {
            ToastUtil.getShortToast(R.string.error_tip, getContext());
        }
    }

}