package com.starkey.engage.ui.learn.data.learn

import com.applications.engage.model.BaseModel
import com.starkey.engage.Utils.clean
import org.json.JSONArray
import org.json.JSONObject

class SearchResultLessonModel : BaseModel() {
    var subTitle = ""
    var description = ""
    var uniqueID = ""
    var contenttype = ""
    var title = ""
    var thumbnailImageUrl = ""
    var videoStillImageUrl = ""
    var videoID = ""
    var duration = ""
    var articles = ArrayList<SearchResultArticleModel>()
    var quizes = ArrayList<SearchResultQuizModel>()
    var videos = ArrayList<SearchResultVideoModel>()

    var jsonObject: JSONObject? = null

    companion object {
        fun mapWithJson(jsonObject: JSONObject) = SearchResultLessonModel().apply {
            this.jsonObject = jsonObject
            uniqueID = jsonObject.optString("UniqueID").clean()
            contenttype = jsonObject.optString("Contenttype").clean()
            title = jsonObject.optString("Title").clean()
            thumbnailImageUrl = jsonObject.optString("ThumbnailImageUrl").clean()
            videoStillImageUrl = jsonObject.optString("VideoStillmageUrl").clean()
            subTitle = jsonObject.optString("SubTitle").clean()
            description = jsonObject.optString("Description").clean()
            videoID = jsonObject.optString("VideoID").clean()
            duration = jsonObject.optString("Duration").clean()

            val articlesArray = jsonObject.optJSONArray("Articles") ?: JSONArray()
            articles = ArrayList(articlesArray.length())
            for (i in 0 until articlesArray.length())
                articles.add(
                    SearchResultArticleModel.mapWithJson(
                        articlesArray.optJSONObject(i) ?: JSONObject()
                    )
                )

            val quizArray = jsonObject.optJSONArray("Quiz") ?: JSONArray()
            quizes = ArrayList(quizArray.length())
            for (i in 0 until quizArray.length())
                quizes.add(
                    SearchResultQuizModel.mapWithJson(
                        quizArray.optJSONObject(i) ?: JSONObject()
                    )
                )

            val videosArray = jsonObject.optJSONArray("Videos") ?: JSONArray()
            videos = ArrayList(videosArray.length())
            for (i in 0 until videosArray.length())
                videos.add(
                    SearchResultVideoModel.mapWithJson(
                        videosArray.optJSONObject(i) ?: JSONObject()
                    )
                )
        }
    }

    override fun getJSON(): JSONObject? {
        return jsonObject
    }
}