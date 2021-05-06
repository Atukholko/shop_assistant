package com.tukholko.assistant.app.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "http://34.118.16.5:8000/";
    private Retrofit mRetrofit;

    private NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public ShopService getShopAPI() {
        return mRetrofit.create(ShopService.class);
    }
    public ProductService getProductAPI() { return mRetrofit.create(ProductService.class); }
    public UserService getUserAPI() { return mRetrofit.create(UserService.class); }
}
