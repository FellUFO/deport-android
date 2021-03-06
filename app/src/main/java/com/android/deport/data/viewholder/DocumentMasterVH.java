package com.android.deport.data.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deport.R;

public class DocumentMasterVH extends FoldableViewHolder {

    public TextView tvName;
    public TextView tvNum;
    public TextView object;
    public ImageView iv;

    public DocumentMasterVH(View view) {
        super(view);
        initView();
    }

    private void initView() {
        tvName = itemView.findViewById(R.id.tv_name);
        object = itemView.findViewById(R.id.object);
        tvNum = itemView.findViewById(R.id.tv_num);
        iv = itemView.findViewById(R.id.iv);
    }
}
