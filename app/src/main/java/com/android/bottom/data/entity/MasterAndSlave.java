package com.android.bottom.data.entity;

import java.util.List;

public class MasterAndSlave {

    private DocumentMaster master;
    private List<DocumentSlave> slaves;

    public DocumentMaster getDocumentMaster() {
        return master;
    }

    public List<DocumentSlave> getSlaves() {
        return slaves;
    }

    public void setMaster(DocumentMaster documentMaster) {
        master = documentMaster;
    }

    public void setSlaves(List<DocumentSlave> documentSlaves) {
        slaves = documentSlaves;
    }

}
