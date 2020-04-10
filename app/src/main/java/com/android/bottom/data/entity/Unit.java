package com.android.bottom.data.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 二级列表数据实体
 * @param <K>
 * @param <V>
 */
public class Unit<K, V> {
    public K group;
    public List<V> children;
    public boolean folded = true;

    public Unit(K group, List<V> children) {
        this.group = group;
        if (children == null) {
            this.children = new ArrayList<>();
        } else {
            this.children = children;
        }
    }

    public Unit(K group, List<V> children, boolean folded) {
        this(group, children);
        this.folded = folded;
    }
}
