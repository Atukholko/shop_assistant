package com.tukholko.assistant.app;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("Product_name")
    public String name;
    @SerializedName("Product_weight")
    public Double weight;
    @SerializedName("Manufacturer")
    public String manufacturer;
    @SerializedName("Product_description")
    public String description;
    @SerializedName("Product_price")
    public Double price;

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", manufacturer='" + manufacturer + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
