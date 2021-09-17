package com.example.android.ktukilite.data

import com.example.android.ktukilite.model.CategoryServerResponse
import com.example.android.ktukilite.model.VideoDetailServerResponse
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DataRetriever {
    private val request: RequestInterface

    companion object {
        private const val BASE_URL = "http://www.mocky.io/v2/"
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        request = retrofit.create(RequestInterface::class.java)
    }

    fun retrieveCategories(callback: Callback<CategoryServerResponse>) {
        request.getCategories().enqueue(callback)
    }

    fun retrieveVideoDetails(callback: Callback<VideoDetailServerResponse>) {
        request.getVideoDetails().enqueue(callback)
    }


}