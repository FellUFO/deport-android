package com.android.deport.data.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deport.R;

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
