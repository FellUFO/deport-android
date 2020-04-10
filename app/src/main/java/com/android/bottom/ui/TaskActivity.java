package com.android.bottom.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.bottom.R;
import com.android.bottom.data.adapters.TaskAdapter;
import com.android.bottom.data.entity.TakeMaster;
import com.android.bottom.data.entity.TakeSlave;
import com.android.bottom.data.entity.TakesUnit;
import com.android.bottom.data.entity.Unit;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaskActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Intent intent = getIntent();
        String takeMasterStr = intent.getStringExtra("takeMasters");
        Log.i("takeMaster", takeMasterStr);
        List<TakeMaster> masters = JSON.parseArray(JSON.parseObject(takeMasterStr).getString("list"), TakeMaster.class);
        for (TakeMaster master : masters) {
            Log.d("Debug", master.getTaskId());
        }
        List<Unit<TakeMaster, TakeSlave>> takes = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_task);
        for (int i = 0; i<masters.size(); i++) {
            Unit<TakeMaster, TakeSlave> unit = new Unit<TakeMaster,TakeSlave>(masters.get(i),masters.get(i).getSlaves());
            takes.add(unit);
        }
        initRecyclerView(takes);
    }

    private void initRecyclerView(List<Unit<TakeMaster, TakeSlave>> takes) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TaskAdapter(this, R.layout.task_master_list, R.layout.task_slave_list,takes);
        recyclerView.setAdapter(mAdapter);
    }


}
