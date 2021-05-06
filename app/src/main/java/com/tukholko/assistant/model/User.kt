package com.tukholko.assistant.model

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("User_email")
        var email: String,
        @SerializedName("User_first_name")
        var firstName: String,
        @SerializedName("User_second_name")
        var secondName: String
        )
