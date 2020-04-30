package com.android.deport.data.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.deport.R;


public class ProductVH extends FoldableViewHolder{
    public TextView needNum;
    public ImageView iv;
    public ConstraintLayout cl;
    public TextView productID;
    public Button btnAgree;
    public Button btnRefuse;


    public ProductVH(@NonNull View itemView) {
        super(itemView);
        initView();
    }


    private void initView() {
        Log.d("Debug", "initView");
        needNum = itemView.findViewById(R.id.need_num);
        productID = itemView.findViewById(R.id.product_id);
        iv = itemView.findViewById(R.id.iv);
        cl = itemView.findViewById(R.id.cl);
        btnAgree =  itemView.findViewById(R.id.btn_agree);
        btnRefuse =  itemView.findViewById(R.id.btn_refuse);
    }


//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_agree:
//                Log.d("onClick", "btn_agree");
//                Log.d("text",productID.getText().toString());
//                //修改，获取数据然后传到修改页面
////                Intent intent = new Intent(mContext, UpdateActivity.class);
////                Bundle bundle = new Bundle();
//
////                mContext.startActivity(intent);
//                break;
//            case R.id.btn_refuse:
//
//
////                delete(productID.getText().toString(),locationNum);
//                break;
//        }
//    }

//    private void delete(String id, String locationNum) {
//        new Thread(() -> {
//            Log.d("Debug", MainActivity.isInternet+"");
//            Log.d("deleteProduct", locationNum);
//            if (MainActivity.isInternet) {
//                Intent intent = new Intent(mContext,LocationActivity.class);
//                mContext.startActivity(intent);
//            }else {
//                DeportRoomDatabase db = DeportRoomDatabase.getDataBase(mContext);
//                ProductLocationDao productLocationDao = db.productLocationDao();
//                productLocationDao.deleteByProductID(id,locationNum);
//                Intent intent = new Intent(mContext,LocationActivity.class);
//                mContext.startActivity(intent);
//            }
//        }).start();
//    }
}
