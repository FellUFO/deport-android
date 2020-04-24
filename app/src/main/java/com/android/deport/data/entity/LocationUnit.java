package com.android.deport.data.entity;

import java.util.List;

public class LocationUnit {

    private List<Location> list;

    private String messages;

    public LocationUnit() {

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
}
