package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class FieldsModel : Serializable {
    @SerializedName("pageTitle")
    @Expose
    private var pageTitle: PageTitleModel? = null

    fun getPageTitle(): PageTitleModel? {
        return pageTitle
    }

    fun setPageTitle(pageTitle: PageTitleModel?) {
        this.pageTitle = pageTitle
    }

}