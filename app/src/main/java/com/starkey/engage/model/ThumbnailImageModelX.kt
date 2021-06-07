package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ThumbnailImageModelX : Serializable {
    @SerializedName("image")
    @Expose
    private var image: ImageModel? = null

    fun getImage(): ImageModel? {
        return image
    }

    fun setImage(image: ImageModel?) {
        this.image = image
    }
}