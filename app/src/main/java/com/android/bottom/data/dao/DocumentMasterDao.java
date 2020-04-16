package com.android.bottom.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.bottom.data.entity.DocumentMaster;

import java.util.Date;
import java.util.List;

@Dao
public interface DocumentMasterDao {

    @Query("select * from document_master where generate between :start and :end")
    LiveData<List<DocumentMaster>> getDocumentByDate(Date start, Date end);

    @Insert
    void insert(DocumentMaster documentMaster);

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
    @Query("SELECT * FROM document_master where state = 0")
    LiveData<List<DocumentMaster>> getDocumentByNotUploaded();

    /**
     * 更改单据状态
     * @param id
     */
    @Query("UPDATE document_master SET state = 1 WHERE order_id = :id")
    void updateDocumentByState(String id);

}
