package com.tukholko.assistant.model

import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("Product_name")
        var name: String,
        @SerializedName("Product_weight")
        var weight: Double,
        @SerializedName("Manufacturer")
        var manufacturer: String,
        @SerializedName("Product_description")
        var description: String,
        @SerializedName("Product_price")
        var price: Double
)
