package com.example.pizzadelivery.model.api.retrofit

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("image") var url : String = "",
)
