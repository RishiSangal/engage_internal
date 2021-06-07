package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class FieldsModel3 : Serializable {

    @SerializedName("Description")
    @Expose
    private var description: DescriptionModel1? = null

    @SerializedName("SubTitle")
    @Expose
    private var subTitle: SubTitleModel1? = null

    @SerializedName("Title")
    @Expose
    private var title: TitleModel2? = null

    @SerializedName("ThumbnailImage")
    @Expose
    private var thumbnailImage: ThumbnailImageModel2? = null

    fun getDescription(): DescriptionModel1? {
        return description
    }

    fun setDescription(description: DescriptionModel1?) {
        this.description = description
    }

    fun getSubTitle(): SubTitleModel1? {
        return subTitle
    }

    fun setSubTitle(subTitle: SubTitleModel1?) {
        this.subTitle = subTitle
    }

    fun getTitle(): TitleModel2? {
        return title
    }

    fun setTitle(title: TitleModel2?) {
        this.title = title
    }

    fun getThumbnailImage(): ThumbnailImageModel2? {
        return thumbnailImage
    }

    fun setThumbnailImage(thumbnailImage: ThumbnailImageModel2?) {
        this.thumbnailImage = thumbnailImage
    }

}