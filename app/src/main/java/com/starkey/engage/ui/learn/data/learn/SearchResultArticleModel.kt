package com.starkey.engage.ui.learn.data.learn

import com.applications.engage.model.BaseModel
import com.starkey.engage.Utils.clean
import org.json.JSONObject

class SearchResultArticleModel : BaseModel() {
    var subTitle = ""
    var description = ""
    var videoID = ""
    var uniqueID = ""
    var contenttype = ""
    var duration = ""
    var videoStillImageUrl = ""
    var title = ""
    var thumbnailImageUrl = ""

    var jsonObject: JSONObject? = null

    companion object {
        fun mapWithJson(jsonObject: JSONObject) = SearchResultArticleModel().apply {
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
        }
    }

    override fun getJSON(): JSONObject? {
        return jsonObject
    }
}