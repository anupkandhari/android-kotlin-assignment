package com.example.android.ktukilite.data

import com.example.android.ktukilite.model.CategoryServerResponse
import com.example.android.ktukilite.model.VideoDetailServerResponse
import retrofit2.Call
import retrofit2.http.GET




interface RequestInterface {
    @GET("5e2bebd23100007a00267e51")
    fun getCategories(): Call<CategoryServerResponse>

    @GET("5e2beb5a3100006600267e4e")
    fun getVideoDetails(): Call<VideoDetailServerResponse>
}