package com.android.bottom.data.adapters;

import android.content.Context;
import android.widget.TextView;

import com.android.bottom.R;
import com.android.bottom.data.entity.TakeMaster;
import com.android.bottom.data.entity.TakeSlave;
import com.android.bottom.data.entity.Unit;
import com.android.bottom.data.viewholder.FoldableViewHolder;
import com.android.bottom.data.viewholder.TaskMasterVH;
import com.android.bottom.data.viewholder.TaskSlaveVH;

import java.io.File;
import java.util.List;

public class TaskAdapter extends FoldableRecyclerViewAdapter<TakeMaster, TakeSlave> {

    public TaskAdapter(Context mContext, int mGroupLayoutRes, int mChildLayoutRes, List<Unit<TakeMaster, TakeSlave>> mData) {
        super(mContext, mGroupLayoutRes, mChildLayoutRes, mData);
    }

    @Override
    public void onBindView(FoldableViewHolder holder, int position) {

    }
//    @Override
//    public void onBindView(FoldableViewHolder holder, int position) {
//        if (holder instanceof TaskMasterVH) {
//            TextView tvName = holder.getView(R.id.tv_name);
//            TextView tvSize = holder.getView(R.id.tv_num);
//
//            TakeMaster directory = (TakeMaster) getItem(position);
//            tvName.setText(directory.getTaskId());
////            tvSize.setText(directory.size+"");
//        }
//
//        if (holder instanceof TaskSlaveVH) {
//            TextView tvName = holder.getView(R.id.tv_child_name);
//            TextView tvSize = holder.getView(R.id.tv_child_num);
//
//            File file = (File) getItem(position);
//            tvName.setText(file.name);
//            tvSize.setText(file.size+"");
//        }
//    }
}
