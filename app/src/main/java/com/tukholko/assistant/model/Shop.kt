package com.tukholko.assistant.model

import com.google.gson.annotations.SerializedName

data class Shop(
        @SerializedName("Local_shop_ID")
        var localShopID: Int,
        @SerializedName("Local_shop_name")
        var localShopName: String,
        @SerializedName("Country")
        var country: String,
        @SerializedName("City")
        var city: String,
        @SerializedName("Address")
        var address: String,
        @SerializedName("Latitude")
        var latitude: Double,
        @SerializedName("Longitude")
        var longitude: Double
)
