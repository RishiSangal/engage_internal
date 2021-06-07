package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ThumbnailImageModel2 : Serializable {
    @SerializedName("value")
    @Expose
    private var value: ValueModel1? = null

    fun getValue(): ValueModel1? {
        return value
    }

    fun setValue(value: ValueModel1?) {
        this.value = value
    }

}
