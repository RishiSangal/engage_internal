package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class SearchBaseModel: Serializable {
    @SerializedName("sitecore")
    @Expose
    private var sitecore: SitecoreModel? = null

    fun getSitecore(): SitecoreModel? {
        return sitecore
    }

    fun setSitecore(sitecore: SitecoreModel?) {
        this.sitecore = sitecore
    }
}