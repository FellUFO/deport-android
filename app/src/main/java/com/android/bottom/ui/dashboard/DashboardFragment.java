package com.android.bottom.ui.dashboard;

import android.content.Intent;
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

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bottom.R;
import com.android.bottom.data.DeportRoomDatabase;
import com.android.bottom.data.adapters.DashboardAdapter;
import com.android.bottom.data.dao.ProductMessageDao;
import com.android.bottom.data.entity.ProductMessage;
import com.android.bottom.service.AlarmService;
import com.android.bottom.ui.AddProductActivity;
import com.android.bottom.utils.DateUtil;
import com.android.bottom.viewmodel.ProductViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.Response;

public class DashboardFragment extends Fragment {

    private ProductViewModel productViewModel;
    private Button add;
    private RecyclerView recyclerView;
    private List<ProductMessage> products = new ArrayList<>();
    private DashboardAdapter mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = root.findViewById(R.id.product_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new DashboardAdapter(getActivity());
        //配置适配器
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllWords().observe(getViewLifecycleOwner(), new Observer<List<ProductMessage>>() {
            @Override
            public void onChanged(List<ProductMessage> productMessages) {
                mAdapter.setVerticalDataList(productMessages);
            }
        });

        add = root.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //发送请求，获得数据
                        String url = "http://192.168.0.116:8080/getWarehouse";
                        OkHttpClient client = new OkHttpClient();
                        final Request request = new Request.Builder().url(url).build();
                        try {
                            Response response = client.newCall(request).execute();
                            if (response.isSuccessful()) {
                                String responseBody = response.body().string();
                                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                                intent.putExtra("responseBody",responseBody);
                                startActivity(intent);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        return root;
    }
}
