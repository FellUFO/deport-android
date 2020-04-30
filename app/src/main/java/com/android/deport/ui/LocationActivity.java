package com.android.deport.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.alibaba.fastjson.JSON;
import com.android.deport.R;
import com.android.deport.data.DeportRoomDatabase;
import com.android.deport.data.adapters.DocumentAdapter;
import com.android.deport.data.adapters.DocumentRecyclerViewAdapter;
import com.android.deport.data.adapters.LocationAdapter;
import com.android.deport.data.adapters.LocationRecyclerviewAdapter;
import com.android.deport.data.dao.LocationDao;
import com.android.deport.data.dao.ProductLocationDao;
import com.android.deport.data.entity.Location;

import com.android.deport.data.entity.ProductLocation;
import com.android.deport.data.entity.ProductMessage;
import com.android.deport.data.entity.Unit;
import com.android.deport.utils.ToastUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationActivity extends AppCompatActivity{

    private String TAG = "LocationActivity";
    private LocationAdapter mAdapter;
    private RecyclerView recycler;
    DeportRoomDatabase db = DeportRoomDatabase.getDataBase(this);
    LocationDao locationDao = db.locationDao();
    ProductLocationDao plDao = db.productLocationDao();
    final String[] menus = new String[]{"新增出库","任务处理"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        recycler = findViewById(R.id.location_list);
        getData();
        getLocalData();
        SwipeRefreshLayout mRefreshLayout = findViewById(R.id.refresh_layout);
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(() -> {
            //先清空数据，再填充
            clearData();
            getFirstData();
            mRefreshLayout.setRefreshing(false);
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        //fab按钮点击事件
        fab.setOnClickListener((view) -> {
            Log.d("Debug", "setOnClickListener");
            LayoutInflater factory = LayoutInflater.from(this);
            final View dialogView = factory.inflate(R.layout.dialog_customize, null);
            final AlertDialog mDialog = new AlertDialog.Builder(this).create();
            mDialog.setView(dialogView);
            Button button = dialogView.findViewById(R.id.add_pro_location);
            button.setOnClickListener((v)-> {
                EditText locationNumEdit = dialogView.findViewById(R.id.edit_text_location_num);
                String locationNum = locationNumEdit.getText().toString();
                EditText proIDeEdit = dialogView.findViewById(R.id.edit_text_pro_id);
                String proID = proIDeEdit.getText().toString();
                if("".equals(locationNum)||"".equals(proID)) {
                    ToastUtil.getLongToast("请填写内容再点击按钮！", LocationActivity.this);
                } else {
                        if (MainActivity.isInternet) {
                            new Thread(()->{
                            OkHttpClient client = new OkHttpClient();
                            String url = "http://192.168.0.116:8080/addProductLocation?product="+proID+"&locals="+locationNum;
                            Request request = new Request.Builder().url(url).build();
                            Call call = client.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Log.d("Debug", Objects.requireNonNull(e.getMessage()));
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    Message msg = new Message();
                                    msg.obj = Objects.requireNonNull(response.body()).string();
                                    secondHandler.sendMessage(msg);
                                    }
                                });
                            }).start();
                        }else {
                            ToastUtil.getLongToast("网络连接已断开，无法进行修改!", LocationActivity.this);
                        }
                }
            });
            mDialog.show();

        });
    }

    private void initRecyclerView(List<Unit<Location, ProductMessage>> units) {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LocationAdapter(this, R.layout.task_master_list, R.layout.task_slave_list, units);
        recycler.setAdapter(mAdapter);
    }

    /**
     * 清空数据
     */
    private void clearData() {
        new Thread(() -> {
            locationDao.clear();
            plDao.clear();
            Log.d("Debug", "clearData");
        }).start();
    }


    /**
     * 从后台获取数据
     */
    public void getData() {
        new Thread( () ->{
            List<ProductLocation> all = plDao.getAll();
            if (all.isEmpty()) {
                getFirstData();
            }
        }).start();
    }

    public void getLocalData() {
        new Thread(() -> {
            //根据库位去查当前库位有哪些商品存放
            List<Unit<Location, ProductMessage>> units = new ArrayList<>();
            List<Location> allLocation = locationDao.getAllLocation();
            for (Location location : allLocation) {
                List<ProductMessage> products = plDao.getProductIDByLocation(location.getLocationNumber());
                units.add(new Unit<Location, ProductMessage>(location, products));
            }
            Message msg = new Message();
            msg.obj = units;
            handler.sendMessage(msg);
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "handler执行");
            initRecyclerView((List<Unit<Location, ProductMessage>>) msg.obj);
        }
    };

    Handler secondHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "secondHandler");
            ToastUtil.getLongToast((String) msg.obj, LocationActivity.this);
            onResume();
        }
    };


    public void getFirstData() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "http://192.168.0.116:8080/getAllLocation";
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String data = Objects.requireNonNull(response.body()).string();
                    Log.d("location", data);
                    List<Location> locations = JSON.parseArray(JSON.parseObject(data).getString("list"), Location.class);
                    List<ProductLocation> productLocations = JSON.parseArray(JSON.parseObject(data).getString("locationAndProducts"), ProductLocation.class);
                    //存到本地数据库
                    List<Long> longList = plDao.insertList(productLocations);
                    List<Long> longs = locationDao.insertList(locations);
                    if (!longs.isEmpty() && !longList.isEmpty()) {
                        Log.d(TAG, "第一次查询成功！");
                    }
                }
            });
        }).start();
    }



}