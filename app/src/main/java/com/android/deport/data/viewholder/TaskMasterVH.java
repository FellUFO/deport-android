package com.android.deport.data.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deport.R;

public class TaskMasterVH extends FoldableViewHolder  {

    private TextView tvName;
    private TextView tvNum;
    private ImageView iv;

    public TaskMasterVH(View view) {
        super(view);
        initView();
    }

    private void initView() {
        tvName = itemView.findViewById(R.id.tv_name);
        tvNum = itemView.findViewById(R.id.tv_num);
        iv = itemView.findViewById(R.id.iv);
        TextView textView = itemView.findViewById(R.id.object);
        textView.setVisibility(View.INVISIBLE);

    }
}
