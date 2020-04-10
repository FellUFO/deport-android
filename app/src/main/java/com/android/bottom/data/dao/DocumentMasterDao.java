package com.android.bottom.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.bottom.data.entity.DocumentMaster;

import java.util.Date;
import java.util.List;

@Dao
public interface DocumentMasterDao {

    @Query("select * from document_master where generate between :start and :end")
    LiveData<List<DocumentMaster>> getDocumentByDate(Date start, Date end);

    @Insert
    void insert(DocumentMaster documentMaster);

}
