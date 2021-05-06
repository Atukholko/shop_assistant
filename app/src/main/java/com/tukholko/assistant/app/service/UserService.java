package com.tukholko.assistant.app.service;

import com.tukholko.assistant.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("/user")
    Call<User> postData(@Body User data);
}
