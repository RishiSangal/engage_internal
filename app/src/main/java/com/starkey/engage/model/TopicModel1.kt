package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TopicModel1 : Serializable {
    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("quiz")
    @Expose
    private var quiz: QuizModel? = null

    @SerializedName("contents")
    @Expose
    private var contents: ContentsModel? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getQuiz(): QuizModel? {
        return quiz
    }

    fun setQuiz(quiz: QuizModel?) {
        this.quiz = quiz
    }

    fun getContents(): ContentsModel? {
        return contents
    }

    fun setContents(contents: ContentsModel?) {
        this.contents = contents
    }

}
