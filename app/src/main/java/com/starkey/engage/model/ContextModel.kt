package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ContextModel : Serializable {
    @SerializedName("visitorIdentificationTimestamp")
    @Expose
    private var visitorIdentificationTimestamp:Long? = null

    @SerializedName("pageEditing")
    @Expose
    private var pageEditing: Boolean? = null

    @SerializedName("site")
    @Expose
    private var site: SiteModel? = null

    @SerializedName("pageState")
    @Expose
    private var pageState: String? = null

    @SerializedName("language")
    @Expose
    private var language: String? = null

    fun getVisitorIdentificationTimestamp(): Long? {
        return visitorIdentificationTimestamp
    }

    fun setVisitorIdentificationTimestamp(visitorIdentificationTimestamp: Long?) {
        this.visitorIdentificationTimestamp = visitorIdentificationTimestamp
    }

    fun getPageEditing(): Boolean? {
        return pageEditing
    }

    fun setPageEditing(pageEditing: Boolean?) {
        this.pageEditing = pageEditing
    }

    fun getSite(): SiteModel? {
        return site
    }

    fun setSite(site: SiteModel?) {
        this.site = site
    }

    fun getPageState(): String? {
        return pageState
    }

    fun setPageState(pageState: String?) {
        this.pageState = pageState
    }

    fun getLanguage(): String? {
        return language
    }

    fun setLanguage(language: String?) {
        this.language = language
    }
}