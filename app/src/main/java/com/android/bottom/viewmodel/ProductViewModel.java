package com.android.bottom.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.bottom.data.entity.ProductMessage;
import com.android.bottom.data.repository.ProductRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository mProductRepository;

    private LiveData<List<ProductMessage>> mProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        mProductRepository = new ProductRepository(application);
        mProducts = mProductRepository.getAllProductMessage();
    }

    public LiveData<List<ProductMessage>> getAllProducts() { return mProducts;}

}
