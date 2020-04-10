package com.android.bottom.data.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.bottom.R;
import com.android.bottom.data.entity.TakeMaster;
import com.android.bottom.data.entity.TakeSlave;
import com.android.bottom.data.entity.Unit;
import com.android.bottom.data.viewholder.FoldableViewHolder;
import com.android.bottom.data.viewholder.TaskMasterVH;
import com.android.bottom.data.viewholder.TaskSlaveVH;


import java.util.List;

public class TaskAdapter extends FoldableRecyclerViewAdapter<TakeMaster, TakeSlave> {

    public TaskAdapter(Context mContext, int mGroupLayoutRes, int mChildLayoutRes, List<Unit<TakeMaster, TakeSlave>> mData) {
        super(mContext, mGroupLayoutRes, mChildLayoutRes, mData);
    }

    @Override
    public void onBindView(FoldableViewHolder holder, int position) {
        if (holder instanceof TaskMasterVH) {
            TextView tvName = holder.getView(R.id.tv_name);
            TakeMaster directory = (TakeMaster) getItem(position);
            tvName.setText(directory.getTaskId());
        }

        if (holder instanceof TaskSlaveVH) {
            TextView tvName = holder.getView(R.id.product_id);
            TextView tvSize = holder.getView(R.id.need_num);
            TextView working = holder.getView(R.id.working);
            TakeSlave slave = (TakeSlave) getItem(position);
                tvName.setText(slave.getProductId());
                tvSize.setText(String.valueOf(slave.getTaskCount()));
        }
    }
}
