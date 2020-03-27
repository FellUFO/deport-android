package com.android.bottom.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bottom.data.entity.ProductMessage;

import java.util.List;

/**
 * 列表适配器
 */
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardHolder> {

    private List<ProductMessage> productMessageList;
    private LayoutInflater layoutInflater;



    public DashboardAdapter(List<ProductMessage> productMessages) {
        productMessageList = productMessages;
    }



    @NonNull
    @Override
    public DashboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = layoutInflater.from(parent.getContext());
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DashboardHolder extends RecyclerView.ViewHolder {
        public DashboardHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
