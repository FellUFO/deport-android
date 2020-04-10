package com.android.bottom.data.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class TakeMaster implements Serializable {

    private String taskId;

    public boolean folded = true;

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

    public TakeMaster(String taskId, Integer state, List<TakeSlave> slaves) {
        this.taskId = taskId;
        this.state = state;
        if (slaves == null) {
            this.slaves = new ArrayList<>();
        } else {
            this.slaves = slaves;
        }
    }

    public TakeMaster(String taskId, boolean folded, Integer state, List<TakeSlave> slaves) {
        this.taskId = taskId;
        this.folded = folded;
        this.state = state;
        this.slaves = slaves;
    }

    public TakeMaster(){}
}