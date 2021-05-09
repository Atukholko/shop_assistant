package com.tukholko.assistant.app.service;

import com.tukholko.assistant.model.Product;
import com.tukholko.assistant.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @POST("/user")
    Call<User> postData(@Body User data);

    @GET("/user/{email}")
    Call<User> getUserByEmail(@Path("email") String email);
}
