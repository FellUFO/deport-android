package com.android.deport.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "document_master")
public class DocumentMaster {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "order_id")
    private String orderId;

    @ColumnInfo(name = "object")
    private String object;

    @ColumnInfo(name = "generate")
    private Date generate;

    @ColumnInfo(name = "operator")
    private Integer operator;

    @ColumnInfo(name = "deport_id")
    private Integer deportId;

    @ColumnInfo(name = "state")
    private Integer state;  //单据类别 0---》未上传；1---》已上传。


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }



    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object == null ? null : object.trim();
    }

    public Date getGenerate() {
        return generate;
    }

    public void setGenerate(Date generate) {
        this.generate = generate;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public Integer getDeportId() {
        return deportId;
    }

    public void setDeportId(Integer deportId) {
        this.deportId = deportId;
    }

    @Override
    public String toString() {
        return "DocumentMaster{" +
                "orderId='" + orderId + '\'' +
                ", object='" + object + '\'' +
                ", generate=" + generate +
                ", operator=" + operator +
                ", deportId=" + deportId +
                ", state=" + state +
                '}';
    }
}