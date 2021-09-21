package com.example.android.ktukilite.model

import com.example.android.ktukilite.di.DaggerApiComponent
import com.example.android.ktukilite.model.schemas.CategoryServerResponse
import com.example.android.ktukilite.model.schemas.VideoDetailServerResponse
import io.reactivex.Single
import javax.inject.Inject


class DataRetrieveService {

    @Inject
    lateinit var api: KtukiApi



    init {
        DaggerApiComponent.create().inject(this)
    }

    fun retrieveCategories() : Single<CategoryServerResponse> {
        return api.getCategories()
    }

    fun retrieveVideoDetails() :Single<VideoDetailServerResponse> {
        return api.getVideoDetails()
    }


}