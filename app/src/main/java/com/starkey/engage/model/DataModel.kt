package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class DataModel :Serializable {
    @SerializedName("search")
    @Expose
    private var search: SearchModel? = null

    fun getSearch(): SearchModel? {
        return search
    }

    fun setSearch(search: SearchModel?) {
        this.search = search
    }
}