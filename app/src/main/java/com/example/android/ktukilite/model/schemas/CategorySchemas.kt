package com.example.android.ktukilite.model.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CategoryServerResponse {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("response")
    @Expose
    var response: CategoryResponse? = null

}

class CategoryResponse {
    @SerializedName("videoCategories")
    @Expose
    var videoCategory: Map<String, CategoryItem>? = null
}

class CategoryItem {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null
}