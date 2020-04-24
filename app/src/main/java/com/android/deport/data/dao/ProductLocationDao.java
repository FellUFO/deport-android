package com.android.deport.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.deport.data.entity.ProductLocation;
import com.android.deport.data.entity.ProductMessage;

import java.util.List;

@Dao
public interface ProductLocationDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertList(List<ProductLocation> list);

    @Query("SELECT * FROM product_location")
    List<ProductLocation> getAll();

    @Query("SELECT * FROM product_message WHERE product_id in( SELECT product_id FROM product_location WHERE location_num = :num)")
    List<ProductMessage> getProductIDByLocation(String num);

    @Query("DELETE FROM product_location")
    void clear();

}
