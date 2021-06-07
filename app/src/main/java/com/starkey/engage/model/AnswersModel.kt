package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class AnswersModel : Serializable{
    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("targetItems")
    @Expose
    private var targetItems: List<TargetItemModel1?>? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getTargetItems(): List<TargetItemModel1?>? {
        return targetItems
    }

    fun setTargetItems(targetItems: List<TargetItemModel1?>?) {
        this.targetItems = targetItems
    }
}