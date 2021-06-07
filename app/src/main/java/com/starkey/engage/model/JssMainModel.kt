package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class JssMainModel : Serializable {
    @SerializedName("uid")
    @Expose
    private var uid: String? = null

    @SerializedName("componentName")
    @Expose
    private var componentName: String? = null

    @SerializedName("dataSource")
    @Expose
    private var dataSource: String? = null

    @SerializedName("params")
    @Expose
    private var params: ParamsModel? = null

    @SerializedName("fields")
    @Expose
    private var fields: FieldsModel1? = null

    fun getUid(): String? {
        return uid
    }

    fun setUid(uid: String?) {
        this.uid = uid
    }

    fun getComponentName(): String? {
        return componentName
    }

    fun setComponentName(componentName: String?) {
        this.componentName = componentName
    }

    fun getDataSource(): String? {
        return dataSource
    }

    fun setDataSource(dataSource: String?) {
        this.dataSource = dataSource
    }

    fun getParams(): ParamsModel? {
        return params
    }

    fun setParams(params: ParamsModel?) {
        this.params = params
    }

    fun getFields(): FieldsModel1? {
        return fields
    }

    fun setFields(fields: FieldsModel1?) {
        this.fields = fields
    }

}