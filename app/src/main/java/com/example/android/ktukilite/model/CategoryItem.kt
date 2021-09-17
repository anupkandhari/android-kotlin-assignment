package com.example.android.ktukilite.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CategoryItem {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null
}