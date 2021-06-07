package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ThumbnailImageModel1 : Serializable {
    @SerializedName("image")
    @Expose
    private var image: ImageModel1? = null

    fun getImage(): ImageModel1? {
        return image
    }

    fun setImage(image: ImageModel1?) {
        this.image = image
    }
}