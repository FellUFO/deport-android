package com.android.deport.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.deport.data.entity.Location;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertList(List<Location> locations);

    @Query("SELECT * FROM location ")
    List<Location> getAllLocation();

    @Query("DELETE FROM location")
    void clear();

}
