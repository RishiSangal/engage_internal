package com.starkey.engage.ui.learn.data.learn

import com.applications.engage.model.BaseModel
import com.applications.engage.model.LessonQuizAnswerModel
import com.starkey.engage.Utils.clean
import org.json.JSONArray
import org.json.JSONObject

class SearchResultQuizModel : BaseModel() {

    var uniqueID = ""
    var contenttype = ""
    var title = ""
    var thumbnailImageUrl = ""
    var quizOptions = ArrayList<LessonQuizAnswerModel>()
    var quizCorrectAnswers = ArrayList<LessonQuizAnswerModel>()
    var jsonObject: JSONObject? = null

    companion object {
        fun mapWithJson(jsonObject: JSONObject) = SearchResultQuizModel().apply {
            this.jsonObject = jsonObject
            uniqueID = jsonObject.optString("UniqueID").clean()
            contenttype = jsonObject.optString("Contenttype").clean()
            title = jsonObject.optString("Title").clean()

            thumbnailImageUrl = jsonObject.optString("ThumbnailImageUrl").clean()
            val optionsArray = jsonObject.optJSONArray("Answers") ?: JSONArray()
            quizOptions = ArrayList(optionsArray.length())
            for (i in 0 until optionsArray.length())
                quizOptions.add(
                    LessonQuizAnswerModel.mapWithJson(
                        optionsArray.optJSONObject(i) ?: JSONObject()
                    )
                )

            val answersArray = jsonObject.optJSONArray("CorrectAnswer") ?: JSONArray()
            quizCorrectAnswers = ArrayList(answersArray.length())
            for (i in 0 until answersArray.length())
                quizCorrectAnswers.add(
                    LessonQuizAnswerModel.mapWithJson(
                        answersArray.optJSONObject(
                            i
                        ) ?: JSONObject()
                    )
                )

        }
    }

    fun getCorrectAnswer(): LessonQuizAnswerModel? {
        return quizCorrectAnswers.firstOrNull()
    }

    override fun getJSON(): JSONObject? {
        return jsonObject
    }
}