package com.android.deport.data.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.android.deport.R;
import com.android.deport.data.entity.Location;
import com.android.deport.data.entity.ProductMessage;
import com.android.deport.data.entity.Unit;
import com.android.deport.data.viewholder.DocumentMasterVH;
import com.android.deport.data.viewholder.DocumentSlaveVH;
import com.android.deport.data.viewholder.FoldableViewHolder;

import java.util.List;

public class LocationAdapter extends DocumentRecyclerViewAdapter<Location, ProductMessage> implements View.OnClickListener{


    public LocationAdapter(Context mContext, int mGroupLayoutRes, int mChildLayoutRes, List<Unit<Location, ProductMessage>> mData) {
        super(mContext, mGroupLayoutRes, mChildLayoutRes, mData);
    }

    @Override
    public void onBindView(FoldableViewHolder holder, int position) {
        if (holder instanceof DocumentMasterVH) {
//            Log.d("DocumentMasterVH","-----------------");
            TextView tvName = holder.getView(R.id.tv_name);
            Location master = (Location) getItem(position);
            tvName.setText(master.getLocationNumber());
        }

        if (holder instanceof DocumentSlaveVH) {
//            Log.d("DocumentSlaveVH","-----------------");
            TextView tvName = holder.getView(R.id.product_id);
            tvName.setMaxLines(Integer.MAX_VALUE);
            TextView tvSize = holder.getView(R.id.need_num);
            Switch aSwitch = holder.getView(R.id.switch1);
            aSwitch.setChecked(true);
            aSwitch.setEnabled(false);
            ProductMessage slave = (ProductMessage) getItem(position);
            tvName.setText(slave.getProductId());
            tvSize.setText(String.valueOf(slave.getCount()));
        }
    }


    //item里面有多个控件可以点击
    public enum  ViewName {
        ITEM,
        PRACTISE
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, DocumentAdapter.ViewName viewName, int position);
        void onItemLongClick(View v);
    }

    private DocumentAdapter.OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并暴露给外面的调用者
    public void setOnItemClickListener(DocumentAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(); //getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                case R.id.recycler_offline_order:
                    mOnItemClickListener.onItemClick(v, DocumentAdapter.ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, DocumentAdapter.ViewName.ITEM, position);
                    break;
            }
        }
    }
}
