package com.example.android.testtaskdelivery.data.api

import com.example.pizzadelivery.model.api.retrofit.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api{

    @GET("everything?q=bitcoin&apiKey=d28bdf1829004404a3186ac07e1a185c")
    fun fetchNews(): Call<NewsResponse>
}

//d28bdf1829004404a3186ac07e1a185c