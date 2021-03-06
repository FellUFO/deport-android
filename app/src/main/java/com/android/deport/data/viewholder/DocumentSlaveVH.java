package com.android.deport.data.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.deport.R;

public class DocumentSlaveVH extends FoldableViewHolder {
    public TextView needNum;
    public ImageView iv;
    public ConstraintLayout cl;
    public TextView productID;
    public Button btnAgree;
    public Button btnRefuse;


    public DocumentSlaveVH(@NonNull View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        needNum = itemView.findViewById(R.id.need_num);
        productID = itemView.findViewById(R.id.product_id);
        iv = itemView.findViewById(R.id.iv);
        cl = itemView.findViewById(R.id.cl);
        btnAgree =  itemView.findViewById(R.id.btn_agree);
        btnRefuse =  itemView.findViewById(R.id.btn_refuse);
        btnAgree.setVisibility(View.INVISIBLE);
        btnRefuse.setVisibility(View.INVISIBLE);
    }
}
