package com.tukholko.assistant.app;

import com.google.gson.annotations.SerializedName;

public class Shop {
    @SerializedName("Local_shop_ID")
    public Integer localShopID;
    @SerializedName("Local_shop_name")
    public String localShopName;
    @SerializedName("Latitude")
    public Double latitude;
    @SerializedName("Longitude")
    public Double longitude;
}
