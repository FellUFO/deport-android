package com.android.bottom.data.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.android.bottom.R;
import com.android.bottom.data.entity.DocumentMaster;
import com.android.bottom.data.entity.DocumentSlave;
import com.android.bottom.data.entity.Unit;
import com.android.bottom.data.viewholder.DocumentMasterVH;
import com.android.bottom.data.viewholder.DocumentSlaveVH;
import com.android.bottom.data.viewholder.FoldableViewHolder;

import java.util.List;

public class DocumentAdapter extends DocumentRecyclerViewAdapter<DocumentMaster, DocumentSlave>
        implements View.OnClickListener{

    public DocumentAdapter(Context mContext, int mGroupLayoutRes, int mChildLayoutRes, List<Unit<DocumentMaster, DocumentSlave>> mData) {
        super(mContext, mGroupLayoutRes, mChildLayoutRes, mData);
    }
    private final String[] objects = {"入库", "出库"};
    private String symbol = "";
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindView(FoldableViewHolder holder, int position) {
        if (holder instanceof DocumentMasterVH) {
            Log.d("DocumentMasterVH","-----------------");
            TextView tvName = holder.getView(R.id.tv_name);
            DocumentMaster master = (DocumentMaster) getItem(position);
            tvName.setText(master.getOrderId());
            if (objects[0].equals(master.getObject())) {
                symbol = "+";
            }
            if (objects[1].equals(master.getObject())) {
                symbol = "-";
            }
        }

        if (holder instanceof DocumentSlaveVH) {
            Log.d("DocumentSlaveVH","-----------------");
            TextView tvName = holder.getView(R.id.product_id);
            tvName.setMaxLines(Integer.MAX_VALUE);
            TextView tvSize = holder.getView(R.id.need_num);
            Switch aSwitch = holder.getView(R.id.switch1);
            aSwitch.setChecked(true);
            aSwitch.setEnabled(false);
            DocumentSlave slave = (DocumentSlave) getItem(position);
            tvName.setText(slave.getProductId());
            tvSize.setText(symbol + slave.getCount());
        }
    }

    //item里面有多个控件可以点击
    public enum  ViewName {
        ITEM,
        PRACTISE
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, ViewName viewName, int position);
        void onItemLongClick(View v);
    }

    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并暴露给外面的调用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(); //getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                case R.id.recycler_offline_order:
                    mOnItemClickListener.onItemClick(v, ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, ViewName.ITEM, position);
                    break;
            }
        }
    }


}
