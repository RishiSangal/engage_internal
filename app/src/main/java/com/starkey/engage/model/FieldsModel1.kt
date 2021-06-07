package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class FieldsModel1 : Serializable {
    @SerializedName("data")
    @Expose
    private var data: DataModel? = null

    @SerializedName("items")
    @Expose
    private var items: List<ItemModel?>? = null

    fun getData(): DataModel? {
        return data
    }

    fun setData(data: DataModel?) {
        this.data = data
    }

    fun getItems(): List<ItemModel?>? {
        return items
    }

    fun setItems(items: List<ItemModel?>?) {
        this.items = items
    }
}