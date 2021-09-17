package com.example.android.ktukilite.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CategoryResponse {
    @SerializedName("videoCategories")
    @Expose
    var videoCategory: Map<String, CategoryItem>? = null
}