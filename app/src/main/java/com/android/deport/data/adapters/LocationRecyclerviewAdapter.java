package com.android.deport.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.deport.R;
import com.android.deport.data.viewholder.LocationVH;
import com.android.deport.data.entity.Unit;
import com.android.deport.data.viewholder.FoldableViewHolder;
import com.android.deport.data.viewholder.ProductVH;

import java.util.ArrayList;
import java.util.List;

public abstract class LocationRecyclerviewAdapter<K, V> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    /**
     * 上级布局
     */
    private int mGroupLayoutRes;

    /**
     * 下级布局
     */
    private int mChildLayoutRes;

    /**
     * 数据
     */
    private List<Unit<K, V>> mData;

    /**
     * 点击与长按监听接口
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }


    LocationRecyclerviewAdapter(Context mContext, int mGroupLayoutRes, int mChildLayoutRes, List<Unit<K, V>> mData) {
        this.mContext = mContext;
        this.mGroupLayoutRes = mGroupLayoutRes;
        this.mChildLayoutRes = mChildLayoutRes;
        if (mData == null) {
            this.mData = new ArrayList<>();
        } else {
            this.mData = mData;
        }
    }

    /**
     * 无参构造
     */
    public LocationRecyclerviewAdapter() {}

    @Override
    public int getItemCount() {
        if (mSize == 0) {
            int totalSize = 0;
            for (Unit unit : mData) {
                totalSize += (unit.folded ? 1 : unit.children.size() + 1);
            }
            mSize = totalSize;
        }
        return mSize;
    }

    private int mSize = 0;

    @Override
    public int getItemViewType(int position) {
        //通过位置判断type，因为数据传入后顺序不变，可通过数据来判断当前位置是哪一类数据
        int currentPosition = -1;
        for (Unit unit : mData) {
            if (unit.folded) {
                currentPosition = currentPosition + 1;
                if (currentPosition == position) {
                    return FoldableViewHolder.GROUP;
                }
            } else {
                //算上group
                currentPosition = currentPosition + 1;
                if (currentPosition == position) {
                    return FoldableViewHolder.GROUP;
                }
                //算上children，通过比较大小确定是否是当前Unit中的child
                currentPosition = currentPosition + unit.children.size();
                if (position <= currentPosition) {
                    return FoldableViewHolder.CHILD;
                }
            }
        }
        return FoldableViewHolder.GROUP;
    }

    /**
     *  根据索引返回Unit中的K或V
     * @param position 索引
     * @return K/V
     */
     Object getItem(int position) {
        int currentPosition = -1;
        for (Unit unit : mData) {
            if (unit.folded) {
                currentPosition = currentPosition + 1;
                if (currentPosition == position) {
                    return unit.group;
                }
            } else {
                //算上group
                currentPosition = currentPosition + 1;
                if (currentPosition == position) {
                    return unit.group;
                }
                //算上children，通过计算确定是当前Unit的child的索引
                currentPosition = currentPosition + unit.children.size();
                if (position <= currentPosition) {
                    int unitChildIndex = unit.children.size() - 1 - (currentPosition - position);
                    return unit.children.get(unitChildIndex);
                }
            }
        }
        return null;
    }

    /**
     * 根据索引确定返回某个数据集
     * @param position 索引
     * @return Unit
     */
    private Unit<K,V> getUnit(int position) {
        int currentPosition = -1;
        for (Unit<K,V> unit : mData) {
            //算上group
            currentPosition += unit.folded ? 1 : unit.children.size() + 1;
            if (position <= currentPosition)
                return unit;
        }
        return null;
    }
    private OnItemClickLitener itemClickLitener;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LocationVH locationVH = new LocationVH(LayoutInflater.from(mContext).inflate(mGroupLayoutRes, viewGroup, false));
        if (viewType == FoldableViewHolder.CHILD) {
            return new ProductVH(LayoutInflater.from(mContext).inflate(mChildLayoutRes, viewGroup, false));
        }
        return locationVH;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        viewHolder.itemView.setOnClickListener( (v) ->{
            if (viewHolder instanceof LocationVH) {
                Unit<K,V> unit = getUnit(viewHolder.getAdapterPosition());
                if (unit != null) {
                    unit.folded = !unit.folded;
                    mSize = 0;
                    ImageView iv= ((LocationVH) viewHolder).iv;
                    iv.setImageResource(R.drawable.down_arrow);
                    if(unit.folded){
                        iv.setImageResource(R.drawable.ic_arrow_right);
                        notifyItemRangeRemoved(viewHolder.getAdapterPosition()+1,unit.children.size());
                    }else{
                        iv.setImageResource(R.drawable.down_arrow);
                        notifyItemRangeInserted(viewHolder.getAdapterPosition()+1,unit.children.size());
                    }
                }
            }
            if (itemClickLitener != null)
                itemClickLitener.onItemClick(viewHolder.itemView, viewHolder.getLayoutPosition());
        });
        viewHolder.itemView.setOnLongClickListener( (v) -> {
            if (itemClickLitener != null)
                itemClickLitener.onItemLongClick(viewHolder.itemView, viewHolder.getLayoutPosition());
            return true;
        });
        onBindView((FoldableViewHolder) viewHolder, position);
    }

    abstract void onBindView(FoldableViewHolder holder, int position);




}
