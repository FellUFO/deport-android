package com.android.deport.data.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "product_message")
public class ProductMessage {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "product_id")
    private String productId;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "add_time")
    private Date addTime;

    @ColumnInfo(name = "update_time")
    private Date updateTime;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "count")
    private Integer count;

    @ColumnInfo(name = "warehouse_id")
    private Integer warehouseId;

    @NotNull
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public ProductMessage() {
    }

    @NotNull
    @Override
    public String toString() {
        return "ProductMessage{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", addTime=" + addTime +
                ", updateTime=" + updateTime +
                ", category='" + category + '\'' +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", warehouseId=" + warehouseId +
                '}';
    }
}
