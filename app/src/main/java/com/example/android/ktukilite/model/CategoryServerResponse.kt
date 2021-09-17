package com.example.android.ktukilite.model

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