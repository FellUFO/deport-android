package com.android.bottom.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bottom.R;
import com.android.bottom.data.adapters.DashboardAdapter;
import com.android.bottom.data.entity.ProductMessage;
import com.android.bottom.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.Response;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    List<ProductMessage> productMessages = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = root.findViewById(R.id.product_list);
        initRecyclerView();
        return root;
    }

    private void initRecyclerView() {
        Log.i("DashboardFragment","DashboardFragment");
        //获取所有商品
        requestAllProducts();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DashboardAdapter mAdapter = new DashboardAdapter(getActivity());
        //配置适配器
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        List<String> productNames = new ArrayList<>();
        for (ProductMessage productMessage : productMessages) {
            Log.i("productName",productMessage.getProductName());
            productNames.add(productMessage.getProductName());
        }
        mAdapter.setVerticalDataList(productNames);
    }


    private void requestAllProducts() {
        List products = new ArrayList<>();
        //发起请求
//        192.168.0.116
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("message","创建线程");
                    String url = "http://192.168.0.116:8080/getAllProduct";
                    OkHttpClient client = new OkHttpClient();
                    final Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        JSONObject jsonData = new JSONObject(data);
                        JSONArray jsonArray = jsonData.optJSONArray("data");
                        for (int i =0; i<jsonArray.length(); i++) {
                            ProductMessage productMessage = new ProductMessage();
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            productMessage.setProductId(jsonObject.optString("productId"));
                            productMessage.setProductName(jsonObject.optString("productName"));
                            productMessage.setAddTime(DateUtil.getDate(jsonObject.optString("addTime")));
                            productMessage.setUpdateTime(DateUtil.getDate(jsonObject.optString("updateTime")));
                            productMessage.setCategory(jsonObject.optString("category"));
                            productMessage.setMessage(jsonObject.optString("message"));
                            productMessage.setCount(jsonObject.optInt("count"));
                            productMessage.setWarehouseId(jsonObject.optInt("warehouseId"));
                            productMessages.add(productMessage);
                        }
                        for (ProductMessage message : productMessages) {
                            Log.i("productId",message.getProductId());
                        }
                    }
                        else {
                            Log.i("message", "没有成功");
                        }
                 } catch (Exception e) {
                    e.printStackTrace();
                    }
                }
         }).start();
        }

}
