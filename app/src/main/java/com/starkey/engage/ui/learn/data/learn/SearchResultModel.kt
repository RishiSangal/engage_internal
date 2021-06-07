package com.starkey.engage.ui.learn.data.learn

import com.applications.engage.model.BaseModel
import com.starkey.engage.Utils.clean
import org.json.JSONObject

class SearchResultModel : BaseModel() {

    var uniqueID = ""
    var contenttype = ""
    var title = ""
    var thumbnailImageUrl = ""
    var includeInSearch = ""
    var jsonObject: JSONObject? = null
    var newsType: NewsType? = null

    companion object {
        const val CONTENT_TYPE_VIDEO = "Video"
        const val CONTENT_TYPE_QUIZ = "Quiz"
        const val CONTENT_TYPE_ARTICLE = "Article"
        const val CONTENT_TYPE_LESSON = "Lesson"
        fun mapWithJson(jsonObject: JSONObject) = SearchResultModel().apply {
            this.jsonObject = jsonObject
            uniqueID = jsonObject.optString("UniqueID").clean()
            contenttype = jsonObject.optString("Contenttype").clean()
            title = jsonObject.optString("Title").clean()
            thumbnailImageUrl = jsonObject.optString("ThumbnailImageUrl").clean()
            includeInSearch = jsonObject.optString("IncludeInSearch").clean()

            if (contenttype.equals("Quiz" )){
                newsType = NewsType.Quiz
            } else if (contenttype.equals("Video")){
                newsType = NewsType.Video
            } else if (contenttype.equals("Article")){
                newsType = NewsType.Article
            } else if (contenttype.equals("Lesson")){
                newsType = NewsType.Lesson
            }
        }
    }


    override fun getJSON(): JSONObject? {
        return jsonObject
    }
}