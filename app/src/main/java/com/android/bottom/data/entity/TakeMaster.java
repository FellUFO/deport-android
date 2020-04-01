package com.android.bottom.data.entity;

import java.io.Serializable;
import java.util.List;

public class TakeMaster implements Serializable {
    private String taskId;

    private Integer state;

    private List<TakeSlave> slaves;

    public List<TakeSlave> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<TakeSlave> slaves) {
        this.slaves = slaves;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}