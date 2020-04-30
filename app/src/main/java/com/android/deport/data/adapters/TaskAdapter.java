package com.android.deport.data.adapters;

import android.content.Context;
import android.widget.TextView;

import com.android.deport.R;
import com.android.deport.data.entity.TakeMaster;
import com.android.deport.data.entity.TakeSlave;
import com.android.deport.data.entity.Unit;
import com.android.deport.data.viewholder.FoldableViewHolder;
import com.android.deport.data.viewholder.TaskMasterVH;
import com.android.deport.data.viewholder.TaskSlaveVH;


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

            TakeSlave slave = (TakeSlave) getItem(position);
                tvName.setText(slave.getProductId());
                tvSize.setText(String.valueOf(slave.getTaskCount()));
        }
    }
}
