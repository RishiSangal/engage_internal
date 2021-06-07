package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ImageModel : Serializable {
    @SerializedName("src")
    @Expose
    private var src: String? = null

    fun getSrc(): String? {
        return src
    }

    fun setSrc(src: String?) {
        this.src = src
    }
}