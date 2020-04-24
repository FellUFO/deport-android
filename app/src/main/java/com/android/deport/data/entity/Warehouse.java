package com.android.deport.data.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "warehouse")
public class Warehouse {

    public Warehouse() {
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer warehouseId;

    @ColumnInfo(name = "warehouse_name")
    private String warehouseName;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName == null ? null : warehouseName.trim();
    }
}