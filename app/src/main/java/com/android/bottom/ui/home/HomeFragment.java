package com.android.bottom.ui.home;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.android.bottom.R;
import com.android.bottom.data.entity.ProductMessage;
import com.android.bottom.data.entity.TakeMaster;
import com.android.bottom.ui.DatePickerActivity;
import com.android.bottom.ui.MainActivity;
import com.android.bottom.ui.OrderActivity;
import com.android.bottom.ui.TaskActivity;
import com.android.bottom.ui.order.DocumentFragment;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    final String[] items = new String[]{"xxxxx","新增入库"};
    final String[] menus = new String[]{"新增出库","任务处理"};
    private int index;
    private HomeViewModel homeViewModel;
    private Button enter;
    private Button out;
    private Button historyOrder;
    private Button locationManagement;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        enter = (Button)getActivity().findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAlertDialog();
            }
        });
        out = (Button)getActivity().findViewById(R.id.out);
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("请选择你的操作")
                        .setIcon(R.mipmap.ic_launcher)
                        .setSingleChoiceItems(menus, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                index = i;
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (index){
                                    case 0:
                                        Toast.makeText(getActivity(), "正在载入...", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), OrderActivity.class);
                                        intent.putExtra("object", "出库");
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        if (MainActivity.isInternet){
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    OkHttpClient client = new OkHttpClient();
                                                    String url = "http://192.168.0.116:8080/getTaskByState?state=0";
                                                    final Request request = new Request.Builder().url(url).build();
                                                    Call call = client.newCall(request);
                                                    call.enqueue(new Callback() {
                                                        @Override
                                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                            Log.d("state",e.getMessage());

                                                        }
                                                        //异步请求
                                                        @Override
                                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                            String responseBody = response.body().string();
                                                            Message message = new Message();
                                                            message.obj = responseBody;
                                                            handler.sendMessage(message);
                                                        }
                                                    });
                                                }
                                            }).start();
                                        } else {
                                            Toast.makeText(getActivity(), "网络异常，无法使用此功能！", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                }
                                Toast.makeText(getActivity(), "这是确定按钮" + "点的是：" + items[index], Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getActivity(), "这是取消按钮", Toast.LENGTH_SHORT).show();
                            }
                        }).create();
                alertDialog.show();
            }
        });
        historyOrder = (Button)getActivity().findViewById(R.id.history_order);
        historyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //时间选择器选择时间段
                Intent intent = new Intent(getActivity(), DatePickerActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("请选择你的操作")
                .setIcon(R.mipmap.ic_launcher)
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        index = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (index){
                            case 0:
                                Toast.makeText(getActivity(), "正在打开扫码界面", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(getActivity(), "正在载入...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), OrderActivity.class);
                                intent.putExtra("object", "入库");
                                startActivity(intent);
                                break;
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "这是取消按钮", Toast.LENGTH_SHORT).show();
                    }
                }).create();
        alertDialog.show();
    }

    Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            String takeMaster = (String) msg.obj;
            Log.i("takeMasters", takeMaster);
            Intent intent = new Intent(getActivity(), TaskActivity.class);
            intent.putExtra("takeMasters", takeMaster);
            startActivity(intent);
        }
    };
}
