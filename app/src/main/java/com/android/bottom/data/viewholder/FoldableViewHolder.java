package com.android.bottom.data.viewholder;

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class FoldableViewHolder extends RecyclerView.ViewHolder {


    public static final int GROUP = 0;
    public static final int CHILD = 1;

    private SparseArray<View> views = new SparseArray<>();
    private View convertView;

    public FoldableViewHolder(@NonNull View itemView) {
        super(itemView);
        this.convertView = itemView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int resId) {
        View v = views.get(resId);
        if (null == v) {
            v = convertView.findViewById(resId);
            views.put(resId, v);
        }
        return (T) v;
    }
}
