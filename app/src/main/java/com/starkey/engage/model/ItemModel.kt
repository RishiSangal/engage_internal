package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ItemModel : Serializable {
    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("url")
    @Expose
    private var url: String? = null

    @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("displayName")
    @Expose
    private var displayName: String? = null

    @SerializedName("fields")
    @Expose
    private var fields: FieldsModel2? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String?) {
        this.url = url
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getDisplayName(): String? {
        return displayName
    }

    fun setDisplayName(displayName: String?) {
        this.displayName = displayName
    }

    fun getFields(): FieldsModel2? {
        return fields
    }

    fun setFields(fields: FieldsModel2?) {
        this.fields = fields
    }

}