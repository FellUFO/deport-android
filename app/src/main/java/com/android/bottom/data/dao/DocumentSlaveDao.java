package com.android.bottom.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.bottom.data.entity.DocumentSlave;

import java.util.List;

@Dao
public interface DocumentSlaveDao {

    @Query("select * from document_slave where master_id = :id")
    LiveData<List<DocumentSlave>> getDocumentByMasterId(String id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertListDocument(List<DocumentSlave> slaves);



}
