package com.tukholko.assistant.app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ShopService {
    @GET("products/{id}/{barcode}")
    Call<Product> getProductWithID(@Path("id") int id, @Path("barcode") String barcode);

    @GET("shops")
    Call<List<Shop>> getShops();
}