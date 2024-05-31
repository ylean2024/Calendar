package com.example.pizzadelivery.model.api.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pizzadelivery.model.api.retrofit.response.NewsResponse
import com.example.android.calendar.model.Article
import com.example.android.testtaskdelivery.data.api.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ProductFetch"


class Fetch {
    private val newsApi: Api
    init {

        val retrofitNews: Retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        newsApi = retrofitNews.create(Api::class.java)
    }


    fun fetchNews(): LiveData<List<Article>> {
        val responseLiveData: MutableLiveData<List<Article>> = MutableLiveData()
        val flickrRequest: Call<NewsResponse> = newsApi.fetchNews()

        flickrRequest.enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch news", t)
            }

            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                val newsResponse: NewsResponse? = response.body()
                val articles: List<Article>? = newsResponse?.articles
                responseLiveData.value = articles
                Log.d(TAG, "Response received news")
            }
        })

        return responseLiveData
    }

}