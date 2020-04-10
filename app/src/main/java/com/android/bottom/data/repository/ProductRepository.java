package com.android.bottom.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.android.bottom.data.DeportRoomDatabase;
import com.android.bottom.data.dao.ProductMessageDao;
import com.android.bottom.data.entity.ProductMessage;

import java.util.List;

public class ProductRepository {

    private ProductMessageDao productMessageDao;
    private LiveData<List<ProductMessage>> mProducts;

    public ProductRepository(Application application) {
        DeportRoomDatabase db = DeportRoomDatabase.getDataBase(application);
        productMessageDao = db.productMessageDao();
        mProducts = productMessageDao.selectAllProduct();
    }

    public LiveData<List<ProductMessage>> getAllProductMessage() {
        return mProducts;
    }
}

