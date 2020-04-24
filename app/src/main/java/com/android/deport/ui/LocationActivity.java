package com.android.deport.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.alibaba.fastjson.JSON;
import com.android.deport.R;
import com.android.deport.data.DeportRoomDatabase;
import com.android.deport.data.adapters.LocationAdapter;
import com.android.deport.data.dao.LocationDao;
import com.android.deport.data.dao.ProductLocationDao;
import com.android.deport.data.entity.Location;

import com.android.deport.data.entity.ProductLocation;
import com.android.deport.data.entity.ProductMessage;
import com.android.deport.data.entity.Unit;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationActivity extends AppCompatActivity {

    private int index = 0;
    private String TAG = "LocationActivity";
    private LocationAdapter mAdapter;
    private RecyclerView recycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        recycler = findViewById(R.id.location_list);
        getData();
        getLocalData();
        SwipeRefreshLayout mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(() ->{
            //先清空数据，再填充
            clearData();
            getFirstData();
            mRefreshLayout.setRefreshing(false);
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
        new Thread(() ->{
            DeportRoomDatabase dataBase = DeportRoomDatabase.getDataBase(this);
            LocationDao locationDao = dataBase.locationDao();
            ProductLocationDao productLocationDao = dataBase.productLocationDao();
            locationDao.clear();
            productLocationDao.clear();
        }).start();
    }


    /**
     * 从后台获取数据
     */
    public void getData() {
        if (index < 1) {
            index++;
            getFirstData();
        }
    }

    public void getLocalData() {
        new Thread( () -> {
            DeportRoomDatabase db = DeportRoomDatabase.getDataBase(this);
            LocationDao locationDao = db.locationDao();
            ProductLocationDao plDao = db.productLocationDao();
            List<Location> allLocation = locationDao.getAllLocation();
            //根据库位去查当下有哪些商品存放
            List<Unit<Location, ProductMessage>> units = new ArrayList<>();
            for (Location location : allLocation) {
                List<ProductMessage> products = plDao.getProductIDByLocation(location.getLocationNumber());
                units.add(new Unit<Location, ProductMessage>(location, products));
            }
            Message msg = new Message();
            msg.obj = units;
            handler.sendMessage(msg);
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "handler执行");
            initRecyclerView((List<Unit<Location, ProductMessage>>) msg.obj);
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
                    DeportRoomDatabase db = DeportRoomDatabase.getDataBase(getApplicationContext());
                    LocationDao locationDao = db.locationDao();
                    ProductLocationDao productLocationDao = db.productLocationDao();
                    List<Long> longList = productLocationDao.insertList(productLocations);
                    List<Long> longs = locationDao.insertList(locations);
                    if (!longs.isEmpty() && !longList.isEmpty()) {
                        Log.d(TAG, "第一次查询成功！");
                    }
                }
            });
        }).start();
    }


}
