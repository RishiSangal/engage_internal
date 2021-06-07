package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class QuizModel : Serializable {
    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("targetItems")
    @Expose
    private var targetItems: List<TargetItemModel?>? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getTargetItems(): List<TargetItemModel?>? {
        return targetItems
    }

    fun setTargetItems(targetItems: List<TargetItemModel?>?) {
        this.targetItems = targetItems
    }
}