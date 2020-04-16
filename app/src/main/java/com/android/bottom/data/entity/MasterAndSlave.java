package com.android.bottom.data.entity;

import java.util.List;

public class MasterAndSlave {

    private DocumentMaster master;
    private List<DocumentSlave> slaves;

    public DocumentMaster getMaster() {
        return master;
    }

    public void setMaster(DocumentMaster master) {
        this.master = master;
    }

    public List<DocumentSlave> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<DocumentSlave> slaves) {
        this.slaves = slaves;
    }

    public MasterAndSlave(DocumentMaster master, List<DocumentSlave> slaves) {
        this.master = master;
        this.slaves = slaves;
    }

    public MasterAndSlave() {
    }

    @Override
    public String toString() {
        return "MasterAndSlave{" +
                "master=" + master +
                ", slaves=" + slaves +
                '}';
    }
}
