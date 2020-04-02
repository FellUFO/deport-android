package com.android.bottom.data.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bottom.R;

public class TaskMasterVH extends FoldableViewHolder  {

    public TextView tvName;
    public TextView tvNum;
    public ImageView iv;

    public TaskMasterVH(View view) {
        super(view);
        initView();
    }

    private void initView() {
        tvName = itemView.findViewById(R.id.tv_name);
        tvNum = itemView.findViewById(R.id.tv_num);
        iv = itemView.findViewById(R.id.iv);
    }
}
