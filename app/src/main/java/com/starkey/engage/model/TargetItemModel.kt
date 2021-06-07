package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TargetItemModel : Serializable {
    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("question")
    @Expose
    private var question: QuestionModel? = null

    @SerializedName("answers")
    @Expose
    private var answers: AnswersModel? = null

    @SerializedName("currectAnswer")
    @Expose
    private var currectAnswer: CurrectAnswerModel? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getQuestion(): QuestionModel? {
        return question
    }

    fun setQuestion(question: QuestionModel?) {
        this.question = question
    }

    fun getAnswers(): AnswersModel? {
        return answers
    }

    fun setAnswers(answers: AnswersModel?) {
        this.answers = answers
    }

    fun getCurrectAnswer(): CurrectAnswerModel? {
        return currectAnswer
    }

    fun setCurrectAnswer(currectAnswer: CurrectAnswerModel?) {
        this.currectAnswer = currectAnswer
    }


}
