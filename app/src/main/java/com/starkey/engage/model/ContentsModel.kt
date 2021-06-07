package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ContentsModel: Serializable {
    @SerializedName("content")
    @Expose
    private var content: List<ContentModel>? = null

    fun getContent(): List<ContentModel>? {
        return content
    }

    fun setContent(content: List<ContentModel>?) {
        this.content = content
    }
}