package com.android.bottom.ui.historyorder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.bottom.R;
import com.android.bottom.data.adapters.DocumentAdapter;
import com.android.bottom.data.entity.DocumentMaster;
import com.android.bottom.data.entity.DocumentSlave;
import com.android.bottom.data.entity.MasterAndSlave;
import com.android.bottom.data.entity.Unit;
import com.android.bottom.ui.MainActivity;
import com.android.bottom.utils.DateUtil;
import com.android.bottom.utils.ToastUtil;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OnLineFragment extends Fragment {

    private final static String TAG ="OnLineFragment";
    private RecyclerView recycler;
    private List<Unit<DocumentMaster, DocumentSlave>> documents = new ArrayList<>();
    private DocumentAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history_order, container, false);
        recycler = root.findViewById(R.id.order_list);
        TextView dateText = (TextView) root.findViewById(R.id.textView16);
        dateText.setText(DateUtil.getStringDate(new Date(), "yyyy-MM-dd"));
        if (MainActivity.isInternet) {
            getDate();
        } else {
            ToastUtil.getLongToast(R.string.error_str,getActivity());
        }
        return root;
    }

    /**
     * 初始化RecyclerView
     * @param documents
     */
    private void initRecyclerView(List<Unit<DocumentMaster, DocumentSlave>> documents) {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DocumentAdapter(getActivity(), R.layout.task_master_list, R.layout.task_slave_list,documents);
        recycler.setAdapter(mAdapter);
    }

    /**
     * 后台获取数据获取
     */
    private void getDate() {
        //获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String url = "http://192.168.0.116:8080/getDocumentByState";
                Request request = new Request.Builder().url(url).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        if (e instanceof SocketTimeoutException) {
                            int index = 0;
                            // 重新提交验证   在这里最好限制提交次数
                            if (index <= 5) {
                                client.newCall(call.request()).enqueue(this);
                                index++;
                            }
                            Log.e("SocketTimeoutException",e.getMessage());
                        }
                        if (e instanceof ConnectException) {
                            Log.e("frost_connection",e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        //得到响应后的回调
                        String data = response.body().string();
                        Log.d(TAG, data);
                        ArrayList<MasterAndSlave> masterAndSlaves = JSON.parseObject(data, new TypeReference<ArrayList<MasterAndSlave>>() {

                        });
                        for (MasterAndSlave masterAndSlave : masterAndSlaves) {
                            Unit<DocumentMaster, DocumentSlave> unit = new Unit<DocumentMaster, DocumentSlave>(
                                    masterAndSlave.getMaster(),
                                    masterAndSlave.getSlaves());
                            documents.add(unit);
                        }
                        Message message = new Message();
                        message.obj = documents;
                        handler.sendMessage(message);
                    }
                });
            }
        }).start();
    }


    /**
     * 更改ui，列表初始化
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("Debug", "handleMessage");
            initRecyclerView((List<Unit<DocumentMaster, DocumentSlave>>) msg.obj);
        }
    };
}