package com.example.android.ktukilite.di

import com.example.android.ktukilite.model.DataRetrieveService
import com.example.android.ktukilite.model.KtukiApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {

    companion object {
        private const val BASE_URL = "http://www.mocky.io/v2/"
    }

    @Provides
    fun provideKtukiApi() : KtukiApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(KtukiApi::class.java)
    }

    @Provides
    fun provideDataRetrieveService() : DataRetrieveService {
        return DataRetrieveService()
    }
}