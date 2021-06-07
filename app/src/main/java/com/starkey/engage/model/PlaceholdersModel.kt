package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class PlaceholdersModel : Serializable {
    @SerializedName("jss-main")
    @Expose
    private var jssMain: List<JssMainModel?>? = null

    fun getJssMain(): List<JssMainModel?>? {
        return jssMain
    }

    fun setJssMain(jssMain: List<JssMainModel?>?) {
        this.jssMain = jssMain
    }
}