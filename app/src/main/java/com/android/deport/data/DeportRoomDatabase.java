package com.android.deport.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.android.deport.data.converter.Converters;
import com.android.deport.data.dao.DocumentMasterDao;
import com.android.deport.data.dao.DocumentSlaveDao;
import com.android.deport.data.dao.LocationDao;
import com.android.deport.data.dao.ProductLocationDao;
import com.android.deport.data.dao.ProductMessageDao;
import com.android.deport.data.dao.WarehouseDao;
import com.android.deport.data.entity.DocumentMaster;
import com.android.deport.data.entity.DocumentSlave;
import com.android.deport.data.entity.Location;
import com.android.deport.data.entity.ProductLocation;
import com.android.deport.data.entity.ProductMessage;
import com.android.deport.data.entity.Warehouse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ProductMessage.class,
                        Location.class,
                        ProductLocation.class,
                        Warehouse.class,
                        DocumentMaster.class,
                        DocumentSlave.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DeportRoomDatabase extends RoomDatabase {

    public abstract ProductMessageDao productMessageDao();
    public abstract LocationDao locationDao();
    public abstract ProductLocationDao productLocationDao();
    public abstract WarehouseDao warehouseDao();
    public abstract DocumentMasterDao documentMasterDao();
    public abstract DocumentSlaveDao documentSlaveDao();

    private static volatile DeportRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static DeportRoomDatabase getDataBase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DeportRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DeportRoomDatabase.class, "deport")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
