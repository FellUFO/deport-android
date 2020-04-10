package com.android.bottom.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.bottom.R;
import com.android.bottom.data.DeportRoomDatabase;
import com.android.bottom.data.dao.DocumentMasterDao;
import com.android.bottom.data.dao.DocumentSlaveDao;
import com.android.bottom.data.entity.DocumentMaster;
import com.android.bottom.data.entity.DocumentSlave;
import com.android.bottom.data.entity.MasterAndSlave;
import com.android.bottom.ui.AddProductActivity;
import com.android.bottom.ui.MainActivity;
import com.android.bottom.ui.OrderActivity;
import com.android.bottom.utils.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationsFragment extends Fragment{

    private NotificationsViewModel notificationsViewModel;
    private Switch aSwitch;
    private Date currentDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        aSwitch = (Switch)root.findViewById(R.id.enable);
        // 从SharedPreferences获取数据:
        SharedPreferences prefs = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
        boolean switchState = prefs.getBoolean("flag", false);
        aSwitch.setChecked(switchState);
        switchState();
        return root;
    }

    private void switchState() {
        try {
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                    if (b) {
                        Toast.makeText(getActivity(),"离线模式已开启！",Toast.LENGTH_SHORT).show();
                        MainActivity.isInternet = false;
                        Log.d("isInternet",MainActivity.isInternet+"");
                        //将数据保存至SharedPreferences:
                        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("flag", true);
                        editor.putString("currentDate", DateUtil.getStringDate(new Date()));
                        editor.commit();
                    } else {
                        Log.d("isInternet",MainActivity.isInternet+"");
                        //将数据保存至SharedPreferences:
                        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("flag", false);
                        editor.commit();
                        currentDate = DateUtil.getDate(preferences.getString("currentDate", ""),"yyyyMMddhhmmss");
                        Log.d("currentDate",DateUtil.getStringDate(currentDate,"yyyy-MM-dd hh:mm:ss"));
                        //从数据库获得数据
                        DeportRoomDatabase db = DeportRoomDatabase.getDataBase(getContext());
                        DocumentMasterDao documentMasterDao = db.documentMasterDao();
                        DocumentSlaveDao documentSlaveDao = db.documentSlaveDao();
                        LiveData<List<DocumentMaster>> documents = documentMasterDao.getDocumentByDate(currentDate, new Date());
                        if (documents.getValue() == null) {
                            Toast.makeText(getActivity(),"无需更新数据！",Toast.LENGTH_SHORT).show();
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                        List<DocumentMaster> masters = documents.getValue();
                                        Log.d("count",masters.size()+"");
                                        List<MasterAndSlave> masterAndSlaves = new ArrayList<>();
                                        for (DocumentMaster master : masters) {
                                            LiveData<List<DocumentSlave>> documentByMasterId = documentSlaveDao.getDocumentByMasterId(master.getOrderId());
                                            List<DocumentSlave> slaves = documentByMasterId.getValue();
                                            MasterAndSlave masterAndSlave = new MasterAndSlave();
                                            masterAndSlave.setMaster(master);
                                            masterAndSlave.setSlaves(slaves);
                                            masterAndSlaves.add(masterAndSlave);
                                        }
                                        //发送请求，获得数据
                                        String url = "http://192.168.0.116:8080/createListDocument";
                                        final OkHttpClient client = new OkHttpClient();
                                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                        Gson gson = new GsonBuilder()
                                                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                                .create();
                                        String content = gson.toJson(masterAndSlaves);
                                        Log.i("documentMasterJSON",content);
                                        RequestBody body = RequestBody.create(JSON, content);
                                        final Request request = new Request.Builder()
                                                .url(url)
                                                .post(body)
                                                .build();
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
                                                final String responseBody = response.body().string();
                                                Log.i("response", responseBody);
                                            }
                                        });
                                    }
                            }).start();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}