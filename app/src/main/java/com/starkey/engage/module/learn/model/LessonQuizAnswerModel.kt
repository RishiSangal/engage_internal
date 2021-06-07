package com.applications.engage.model

import com.starkey.engage.Utils.clean
import org.json.JSONObject

class LessonQuizAnswerModel : BaseModel() {

    var uniqueID = ""
    var contenttype = ""
    var title = ""
    var thumbnailImageUrl = ""
    var subTitle = ""

    var jsonObject: JSONObject? = null

    companion object {
        fun mapWithJson(jsonObject: JSONObject) = LessonQuizAnswerModel().apply {
            this.jsonObject = jsonObject
            uniqueID = jsonObject.optString("UniqueID").clean()
            contenttype = jsonObject.optString("Contenttype").clean()
            title = jsonObject.optString("Title").clean()
            thumbnailImageUrl = jsonObject.optString("ThumbnailImageUrl").clean()
            subTitle = jsonObject.optString("SubTitle").clean()
        }
    }

    override fun getJSON(): JSONObject? {
        return jsonObject
    }
}