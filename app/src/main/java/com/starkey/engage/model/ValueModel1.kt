package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ValueModel1 : Serializable {
    @SerializedName("src")
    @Expose
    private var src: String? = null

    @SerializedName("alt")
    @Expose
    private var alt: String? = null

    @SerializedName("width")
    @Expose
    private var width: String? = null

    @SerializedName("height")
    @Expose
    private var height: String? = null

    fun getSrc(): String? {
        return src
    }

    fun setSrc(src: String?) {
        this.src = src
    }

    fun getAlt(): String? {
        return alt
    }

    fun setAlt(alt: String?) {
        this.alt = alt
    }

    fun getWidth(): String? {
        return width
    }

    fun setWidth(width: String?) {
        this.width = width
    }

    fun getHeight(): String? {
        return height
    }

    fun setHeight(height: String?) {
        this.height = height
    }


}
