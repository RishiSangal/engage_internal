package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TitleModel1 : Serializable {


    @SerializedName("value")
    @Expose
    private var value: String? = null

    fun getValue(): String? {
        return value
    }

    fun setValue(value: String?) {
        this.value = value
    }
}
