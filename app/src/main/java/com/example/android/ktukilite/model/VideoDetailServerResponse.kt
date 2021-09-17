package com.example.android.ktukilite.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VideoDetailServerResponse {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("response")
    @Expose
    var response: VideoDetailResponse? = null
}