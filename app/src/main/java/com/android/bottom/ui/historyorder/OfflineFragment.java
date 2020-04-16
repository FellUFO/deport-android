package com.android.bottom.ui.historyorder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.bottom.R;
import com.android.bottom.data.DeportRoomDatabase;
import com.android.bottom.data.adapters.DocumentAdapter;
import com.android.bottom.data.dao.DocumentMasterDao;
import com.android.bottom.data.dao.DocumentSlaveDao;
import com.android.bottom.data.entity.DocumentMaster;
import com.android.bottom.data.entity.DocumentSlave;
import com.android.bottom.data.entity.MasterAndSlave;
import com.android.bottom.data.entity.Unit;
import com.android.bottom.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class OfflineFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recycler;
    private Button upload;
    private List<Unit<DocumentMaster, DocumentSlave>> date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.offline_history_order, container, false);
        recycler = root.findViewById(R.id.recycler_offline_order);
        upload = root.findViewById(R.id.upload);
        //获取数据
        date = getDate();
        //初始化列表
        initRecyclerView(date);

        return root;
    }

    /**
     * 获取数据
     * @return
     */
    private List<Unit<DocumentMaster, DocumentSlave>> getDate() {
        DeportRoomDatabase db = DeportRoomDatabase.getDataBase(getContext());
        DocumentMasterDao dmDao = db.documentMasterDao();
        DocumentSlaveDao dsDao = db.documentSlaveDao();
        LiveData<List<DocumentMaster>> documentLiveDate = dmDao.getDocumentByNotUploaded();
        List<DocumentMaster> masters = documentLiveDate.getValue();
        List<Unit<DocumentMaster, DocumentSlave>> units = new ArrayList<>();
        if (masters != null) {
            for (DocumentMaster master : masters) {
                List<DocumentSlave> slaves = dsDao.getDocumentByMasterId(master.getOrderId());
                units.add(new Unit<DocumentMaster, DocumentSlave>(master, slaves));
            }
        }
        return units;
    }

    /**
     * 对RecyclerView进行初始化
     * @param date
     */
    private void initRecyclerView(List<Unit<DocumentMaster, DocumentSlave>> date) {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        DocumentAdapter mAdapter = new DocumentAdapter(getActivity(), R.layout.task_master_list, R.layout.task_slave_list, date);
        recycler.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload:
                if (date != null || date.isEmpty()) {
                    List<MasterAndSlave> masterAndSlaves = new ArrayList<>();
                    for (Unit<DocumentMaster, DocumentSlave> unit : date) {
                        masterAndSlaves.add(new MasterAndSlave(unit.group, unit.children));
                    }
                    //发送请求
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient();
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            Gson gson = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .create();
                            String content = gson.toJson(masterAndSlaves);
                            Log.i("documentMasterJSON",content);
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
                                    String responseBody = response.body().string();
                                    if (responseBody.contains("成功")) {
                                        //改变单据状态
                                        DeportRoomDatabase db = DeportRoomDatabase.getDataBase(getContext());
                                        DocumentMasterDao masterDao = db.documentMasterDao();
                                        for (Unit<DocumentMaster, DocumentSlave> unit : date) {
                                            masterDao.updateDocumentByState(unit.group.getOrderId());
                                        }
                                        ToastUtil.getShortToast(R.string.tip_success, getContext());
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    ToastUtil.getShortToast(R.string.tip_upload, getContext());
                }
                break;
        }
    }
}
