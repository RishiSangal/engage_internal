package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class FieldsModel2 : Serializable {
    @SerializedName("Title")
    @Expose
    private var title: TitleModel1? = null

    @SerializedName("ThumbnailImage")
    @Expose
    private var thumbnailImage: ThumbnailImageModel1? = null

    @SerializedName("items")
    @Expose
    private var items: List<ItemModel1?>? = null

    fun getTitle(): TitleModel1? {
        return title
    }

    fun setTitle(title: TitleModel1?) {
        this.title = title
    }

    fun getThumbnailImage(): ThumbnailImageModel1? {
        return thumbnailImage
    }

    fun setThumbnailImage(thumbnailImage: ThumbnailImageModel1?) {
        this.thumbnailImage = thumbnailImage
    }

    fun getItems(): List<ItemModel1?>? {
        return items
    }

    fun setItems(items: List<ItemModel1?>?) {
        this.items = items
    }
}