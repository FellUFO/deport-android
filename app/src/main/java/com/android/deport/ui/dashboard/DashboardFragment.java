package com.android.deport.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.deport.R;
import com.android.deport.data.adapters.DashboardAdapter;
import com.android.deport.data.entity.ProductMessage;
import com.android.deport.ui.AddProductActivity;
import com.android.deport.viewmodel.ProductViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.Response;

public class DashboardFragment extends Fragment {

    private ProductViewModel productViewModel;
    private Button add;
    private RecyclerView recyclerView;
    private List<ProductMessage> products = new ArrayList<>();
    private DashboardAdapter mAdapter;
    private SearchView searchView;

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
        productViewModel.getAllProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductMessage>>() {
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
        searchView = root.findViewById(R.id.searchView);
        return root;
    }
}
