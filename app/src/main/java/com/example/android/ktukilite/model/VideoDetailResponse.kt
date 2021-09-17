package com.example.android.ktukilite.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VideoDetailResponse {
    @SerializedName("videos")
    @Expose
    var videoDetailList: Map<String, VideoDetailItem>? = null
}