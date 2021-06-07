package com.starkey.engage.ui.learn.data.learn

import com.applications.engage.model.BaseModel
import com.starkey.engage.Utils.clean
import org.json.JSONObject

open class SearchResultVideoModel : BaseModel() {
    var uniqueID = ""
    var contenttype = ""
    var title = ""
    var thumbnailImageUrl = ""
    var videoStillImageUrl = ""
    var subTitle = ""
    var description = ""
    var videoID = ""
    var duration = ""
    var jsonObject: JSONObject? = null

    companion object {
        fun mapWithJson(jsonObject: JSONObject) = SearchResultVideoModel().apply {
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