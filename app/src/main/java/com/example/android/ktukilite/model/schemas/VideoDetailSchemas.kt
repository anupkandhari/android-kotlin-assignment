package com.example.android.ktukilite.model.schemas

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VideoDetailItem() : Parcelable {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("thumbnailURL")
    @Expose
    var thumbnailURL: String? = null

    @SerializedName("videoURL")
    @Expose
    var videoURL: String? = null

    @SerializedName("categories")
    @Expose
    var categories: String? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        description = parcel.readString()
        thumbnailURL = parcel.readString()
        videoURL = parcel.readString()
        categories = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(thumbnailURL)
        parcel.writeString(videoURL)
        parcel.writeString(categories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoDetailItem> {
        override fun createFromParcel(parcel: Parcel): VideoDetailItem {
            return VideoDetailItem(parcel)
        }

        override fun newArray(size: Int): Array<VideoDetailItem?> {
            return arrayOfNulls(size)
        }
    }


}

class VideoDetailResponse {
    @SerializedName("videos")
    @Expose
    var videoDetailList: Map<String, VideoDetailItem>? = null
}

class VideoDetailServerResponse {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("response")
    @Expose
    var response: VideoDetailResponse? = null
}