package com.android.bottom.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.android.bottom.R;
import com.android.bottom.data.adapters.DashboardAdapter;
import com.android.bottom.data.adapters.TaskListAdapter;
import com.android.bottom.data.entity.ProductMessage;
import com.android.bottom.data.entity.TakeMaster;
import com.android.bottom.data.entity.TakeSlave;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Intent intent = getIntent();
        String takeMasterStr = intent.getStringExtra("takeMaster");
        Gson gson = new Gson();
        List<TakeMaster> masters = gson.fromJson(takeMasterStr, new TypeToken<List<TakeMaster>>() {
        }.getType());

        recyclerView = (RecyclerView)findViewById(R.id.recycler_task);
        initRecylerView(masters);
    }

    private void initRecylerView(List<TakeMaster> masters) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new TaskListAdapter(this);
        //配置适配器
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        List<String> taskID = new ArrayList<>();
        for (TakeMaster master : masters) {
            taskID.add(master.getTaskId());
        }
        mAdapter.setVerticalDataList(taskID);
    }
}
