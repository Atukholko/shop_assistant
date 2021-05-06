package com.tukholko.assistant.app.service;

import com.tukholko.assistant.model.Product;
import com.tukholko.assistant.model.Shop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ShopService {
    @GET("shops")
    Call<List<Shop>> getShops();
}