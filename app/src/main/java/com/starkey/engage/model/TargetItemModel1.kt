package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TargetItemModel1 : Serializable {
    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("answerText")
    @Expose
    private var answerText: AnswerTextModel? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getAnswerText(): AnswerTextModel? {
        return answerText
    }

    fun setAnswerText(answerText: AnswerTextModel?) {
        this.answerText = answerText
    }
}