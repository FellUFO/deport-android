package com.android.deport.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.deport.data.entity.DocumentMaster;

import java.util.Date;
import java.util.List;

@Dao
public interface DocumentMasterDao {

    @Query("select * from document_master where generate between :start and :end")
    LiveData<List<DocumentMaster>> getDocumentByDate(Date start, Date end);

    @Insert
    Long insert(DocumentMaster documentMaster);

    /**
     * 查询当天的单据
     * @return
     */
    @Query("SELECT * FROM document_master WHERE strftime('%m-%d','now','localtime') = strftime('%m-%d',generate)")
    LiveData<List<DocumentMaster>> getNowDayDocument();

    /**
     * 查询未上传订单
     * @return
     */
    @Query("SELECT * FROM document_master WHERE state = 0")
    List<DocumentMaster> getDocumentByNotUploaded();

    /**
     * 更改单据状态
     * @param id
     */
    @Query("UPDATE document_master SET state = 1 WHERE order_id = :id")
    void updateDocumentByState(String id);

    @Query("DELETE FROM document_master")
    void deleteAll();

}
