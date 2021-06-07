package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TopicModel : Serializable {

    @SerializedName("id")
    @Expose
    private var id: String? = null

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
    private var thumbnailimage: ThumbnailImageModel1? = null

    @SerializedName("topic")
    @Expose
    private var topic: TopicModel1? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
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

    fun getThumbnailimage(): ThumbnailImageModel1? {
        return thumbnailimage
    }

    fun setThumbnailimage(thumbnailimage: ThumbnailImageModel1?) {
        this.thumbnailimage = thumbnailimage
    }

    fun getTopic(): TopicModel1? {
        return topic
    }

    fun setTopic(topic: TopicModel1?) {
        this.topic = topic
    }
}
