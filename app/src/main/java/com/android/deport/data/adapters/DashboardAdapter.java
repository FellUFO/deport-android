package com.android.deport.data.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.deport.R;
import com.android.deport.data.entity.ProductMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表适配器
 */
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardHolder> {
    private static final String TAG = DashboardAdapter.class.getSimpleName();

    private Context mContext;

    private List<ProductMessage> mList = new ArrayList<>();

    public DashboardAdapter(Context context) {
        mContext = context;
    }

    public void setVerticalDataList(List<ProductMessage> list) {
        Log.d(TAG , "setVerticalDataList: " + list.size());
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DashboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.vertical_recycle_item, parent, false);
        return new DashboardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardHolder holder, int position) {
        holder.tvNum.setText(position + 1 + "");
        holder.tvContent.setText(mList.get(position).getProductName());
        holder.nowCount.setText(String.valueOf(mList.get(position).getCount()));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class DashboardHolder extends RecyclerView.ViewHolder {

        TextView tvNum, tvContent, nowCount;

        public DashboardHolder(View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.tv_num);
            tvContent = itemView.findViewById(R.id.tv_content);
            nowCount = itemView.findViewById(R.id.now_count);
        }
    }
}
