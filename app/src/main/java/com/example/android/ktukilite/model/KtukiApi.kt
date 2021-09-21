package com.example.android.ktukilite.model

import com.example.android.ktukilite.model.schemas.CategoryServerResponse
import com.example.android.ktukilite.model.schemas.VideoDetailServerResponse
import io.reactivex.Single
import retrofit2.http.GET




interface KtukiApi {
    @GET("5e2bebd23100007a00267e51")
    fun getCategories(): Single<CategoryServerResponse>

    @GET("5e2beb5a3100006600267e4e")
    fun getVideoDetails(): Single<VideoDetailServerResponse>
}