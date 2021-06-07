package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ContentModel : Serializable {
    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("thumbnailImage")
    @Expose
    private var thumbnailImage: ThumbnailImageModel1? = null

    @SerializedName("title")
    @Expose
    private var title: TitleModel? = null

    @SerializedName("subTitle")
    @Expose
    private var subTitle: SubTitleModel? = null

    @SerializedName("description")
    @Expose
    private var description: DescriptionModel? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getThumbnailImage(): ThumbnailImageModel1? {
        return thumbnailImage
    }

    fun setThumbnailImage(thumbnailImage: ThumbnailImageModel1?) {
        this.thumbnailImage = thumbnailImage
    }

    fun getTitle(): TitleModel? {
        return title
    }

    fun setTitle(title: TitleModel?) {
        this.title = title
    }

    fun getSubTitle(): SubTitleModel? {
        return subTitle
    }

    fun setSubTitle(subTitle: SubTitleModel?) {
        this.subTitle = subTitle
    }

    fun getDescription(): DescriptionModel? {
        return description
    }

    fun setDescription(description: DescriptionModel?) {
        this.description = description
    }
}