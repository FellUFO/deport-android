package com.android.bottom.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.bottom.R;
import com.android.bottom.data.adapters.FoldableRecyclerViewAdapter;
import com.android.bottom.data.entity.TakeMaster;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoldableRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
//        Intent intent = getIntent();
//        String takeMasterStr = intent.getStringExtra("takeMasters");
//        Log.i("takeMaster", takeMasterStr);
//        Gson gson = new Gson();
//        Map map =  gson.fromJson(takeMasterStr, Map.class);
//        List<TakeMaster> masters = (List<TakeMaster>) map.get("data");
//        recyclerView = (RecyclerView)findViewById(R.id.recycler_task);
//        initRecylerView(masters);
    }

//    private void initRecylerView(List<TakeMaster> masters) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////        FoldableRecyclerViewAdapter adapter = new FoldableRecyclerViewAdapter();
//        recyclerView.setAdapter(adapter);
//        adapter.setDatas(masters);
//    }
}
