package com.example.pizzadelivery.model.api.retrofit.response

import com.google.gson.annotations.SerializedName
import com.example.android.calendar.model.Article


data class NewsResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("totalResults") val totalResults: Int?,
    @SerializedName("articles") val articles: List<Article>?
)