package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class VideoModel : Serializable {

    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("videoID")
    @Expose
    private var videoID: String? = null

    @SerializedName("title")
    @Expose
    private var title: String? = null

    @SerializedName("subTitle")
    @Expose
    private var subTitle: String? = null

    @SerializedName("description")
    @Expose
    private var description: String? = null

    @SerializedName("thumbnailimage")
    @Expose
    private var thumbnailimage: ThumbnailImageModelX? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getVideoID(): String? {
        return videoID
    }

    fun setVideoID(videoID: String?) {
        this.videoID = videoID
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun getSubTitle(): String? {
        return subTitle
    }

    fun setSubTitle(subTitle: String?) {
        this.subTitle = subTitle
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String?) {
        this.description = description
    }

    fun getThumbnailimage(): ThumbnailImageModelX? {
        return thumbnailimage
    }

    fun setThumbnailimage(thumbnailimage: ThumbnailImageModelX?) {
        this.thumbnailimage = thumbnailimage
    }
}
