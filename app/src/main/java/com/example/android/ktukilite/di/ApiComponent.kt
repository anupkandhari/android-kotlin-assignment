package com.example.android.ktukilite.di

import com.example.android.ktukilite.model.DataRetrieveService
import com.example.android.ktukilite.viewmodel.CustomViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent  {
    fun inject(service : DataRetrieveService)
    fun inject(viewModel : CustomViewModel  )
}