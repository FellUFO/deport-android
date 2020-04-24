package com.android.deport.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.android.deport.data.entity.ProductMessage;

import java.util.List;

@Dao
public interface ProductMessageDao {

    @Query("select * from product_message ")
    LiveData<List<ProductMessage>> selectAllProduct();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertProducts(List<ProductMessage> productMessages);

    @Insert
    void insertProduct(ProductMessage productMessage);

    @Update()
    int updateAll(List<ProductMessage> products);

    @Query("delete from product_message")
    void deleteAll();

    @Query("update product_message set `count` = `count` - :num where product_id = :productId")
    void reduceCountById(int num, String productId);

    @Query("update product_message set `count` = `count` + :num where product_id = :productId")
    void addCountById(int num, String productId);
}
