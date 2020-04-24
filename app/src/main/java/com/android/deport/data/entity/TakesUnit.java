package com.android.deport.data.entity;

import java.util.List;

public class TakesUnit {

    private List<TakeMaster> list;

    private String messages;

    public TakesUnit() {

    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "TakesUnit{" +
                "list=" + list +
                ", messages='" + messages + '\'' +
                '}';
    }
}
