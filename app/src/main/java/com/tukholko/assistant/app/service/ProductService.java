package com.tukholko.assistant.app.service;

import com.tukholko.assistant.model.Product;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductService {
    @GET("products/{id}/{barcode}")
    Call<Product> getProductWithID(@Path("id") int id, @Path("barcode") String barcode);
}
