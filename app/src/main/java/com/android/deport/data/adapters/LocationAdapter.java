package com.android.deport.data.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.android.deport.R;
import com.android.deport.data.DeportRoomDatabase;
import com.android.deport.data.dao.ProductLocationDao;
import com.android.deport.data.entity.Location;
import com.android.deport.data.entity.ProductLocation;
import com.android.deport.data.entity.ProductMessage;
import com.android.deport.data.entity.Unit;
import com.android.deport.data.viewholder.FoldableViewHolder;
import com.android.deport.data.viewholder.LocationVH;
import com.android.deport.data.viewholder.ProductVH;
import com.android.deport.ui.LocationActivity;
import com.android.deport.ui.MainActivity;
import com.google.gson.Gson;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LocationAdapter extends LocationRecyclerviewAdapter<Location, ProductMessage> {

    private OnItemClickLitener itemClickLitener;
    private Context mContext;
    private List<Unit<Location, ProductMessage>> mData;
    private DeportRoomDatabase db;


    public LocationAdapter(Context mContext, int mGroupLayoutRes, int mChildLayoutRes, List<Unit<Location, ProductMessage>> mData) {
        super(mContext, mGroupLayoutRes, mChildLayoutRes, mData);
        this.mContext = mContext;
        this.mData = mData;
        db = DeportRoomDatabase.getDataBase(mContext);
    }


    public void setOnItemClickLitener(OnItemClickLitener itemClickLitener) {
        this.itemClickLitener = itemClickLitener;
    }

    @Override
    public void onBindView(FoldableViewHolder holder, int position) {
        if (holder instanceof LocationVH) {
            TextView tvName = holder.getView(R.id.tv_name);
            Location master = (Location) getItem(position);
            tvName.setText(master.getLocationNumber());
            TextView obj = holder.getView(R.id.object);
            obj.setVisibility(View.INVISIBLE);
        }

        if (holder instanceof ProductVH) {
            TextView tvName = holder.getView(R.id.product_id);
            tvName.setMaxLines(Integer.MAX_VALUE);
            TextView tvSize = holder.getView(R.id.need_num);
            ProductMessage slave = (ProductMessage) getItem(position);
            tvName.setText(slave.getProductId());
            tvSize.setText(String.valueOf(slave.getCount()));
            Button btnAgree = holder.getView(R.id.btn_agree);
            Button btnRefuse = holder.getView(R.id.btn_refuse);
            btnRefuse.setOnClickListener((view) -> {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("确定要删除嘛？")
                        .setIcon(R.mipmap.ic_launcher)
//                    .setSingleChoiceItems(menus, 0, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            //添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Unit<Location, ProductMessage> unit = mData.get(position - 1);
                                if (unit != null) {
                                    Log.d("Debug", "unit != null");
                                    Log.d("Debug", unit.group.getLocationNumber());
                                    List<ProductMessage> productMessages = unit.children;
                                    productMessages.remove(slave);
                                    Log.d("Debug", slave.getProductId());
                                    removeItemData(slave.getProductId(), unit.group.getLocationNumber());
                                    notifyItemRemoved(position);
                                    notifyItemChanged(position);
                                } else {
                                    Log.d("Debug", "unit == null");
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();
                alertDialog.show();

            });
            btnAgree.setVisibility(View.INVISIBLE);
        }
    }


    private void removeItemData(String proID, String locationNum) {
        new Thread(() -> {
            if (MainActivity.isInternet) {
                OkHttpClient client = new OkHttpClient();
                Log.d("Debug", "OkHttpClient");
                String url = "http://192.168.0.116:8080/deleteLocationAndProduct?pro_id=" + proID + "&location_num=" + locationNum;
                Request request = new Request.Builder().url(url).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("Debug", e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.d("Debug", response.body().string());
                        notifyDataSetChanged();
                    }
                });
            } else {
                Log.d("Debug", "removeItemData");
                ProductLocationDao plDao = db.productLocationDao();
                plDao.deleteByProductID(proID, locationNum);
            }

        }).start();
    }


}
