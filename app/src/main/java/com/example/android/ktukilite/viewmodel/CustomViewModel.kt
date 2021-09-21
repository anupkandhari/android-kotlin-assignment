package com.example.android.ktukilite.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.ktukilite.di.DaggerApiComponent
import com.example.android.ktukilite.model.*
import com.example.android.ktukilite.model.schemas.CategoryItem
import com.example.android.ktukilite.model.schemas.CategoryServerResponse
import com.example.android.ktukilite.model.schemas.VideoDetailItem
import com.example.android.ktukilite.model.schemas.VideoDetailServerResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CustomViewModel : ViewModel() {

    @Inject
    lateinit var dataRetrieveService : DataRetrieveService

    init {
        DaggerApiComponent.create().inject(this)
    }

    private val disposable = CompositeDisposable()
    val categories = MutableLiveData<List<CategoryItem>>()
    val videoList = MutableLiveData<List<VideoDetailItem>>()
    val dataLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun fetchData() {
        fetchDataFromWeb()
    }

    private fun fetchDataFromWeb() {
        loading.value = true

        disposable.add(
            dataRetrieveService.retrieveCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CategoryServerResponse>() {
                    override fun onSuccess(result: CategoryServerResponse) {
                        categories.value = (result?.response?.videoCategory?.values?.toList()
                            ?: emptyList())
                        disposable.add(
                            dataRetrieveService.retrieveVideoDetails()
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(object :
                                    DisposableSingleObserver<VideoDetailServerResponse>() {
                                    override fun onSuccess(result: VideoDetailServerResponse) {
                                        videoList.value =
                                            (result?.response?.videoDetailList?.values?.toList()
                                                ?: emptyList())
                                        getVideoListByCategory()
                                        dataLoadError.value = false
                                        loading.value = false

                                    }

                                    override fun onError(e: Throwable) {
                                        dataLoadError.value = true
                                        loading.value = false
                                    }

                                })
                        )

                    }

                    override fun onError(e: Throwable) {
                        dataLoadError.value = true
                        loading.value = false
                    }

                })
        )

    }

    fun  getVideoListByCategory() :Map<String,MutableSet<VideoDetailItem>>  {
        val categorySets : MutableMap<String, MutableSet<VideoDetailItem>> = mutableMapOf()
        categories.value?.let {
            for (item in it)
                categorySets[item.name!!] = mutableSetOf()
        }

        videoList.value?.let {
            for (item in it) {
                var list: List<String> = item.categories?.split(",")?.toList() ?: emptyList()
                for (category in list)
                    categorySets[category]?.add(item)
            }
        }
        return categorySets
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}