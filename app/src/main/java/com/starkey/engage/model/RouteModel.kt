package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RouteModel : Serializable {
    @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("displayName")
    @Expose
    private var displayName: String? = null

    @SerializedName("fields")
    @Expose
    private var fields: FieldsModel? = null

    @SerializedName("databaseName")
    @Expose
    private var databaseName: String? = null

    @SerializedName("deviceId")
    @Expose
    private var deviceId: String? = null

    @SerializedName("itemId")
    @Expose
    private var itemId: String? = null

    @SerializedName("itemLanguage")
    @Expose
    private var itemLanguage: String? = null

    @SerializedName("itemVersion")
    @Expose
    private var itemVersion: Int? = null

    @SerializedName("layoutId")
    @Expose
    private var layoutId: String? = null

    @SerializedName("templateId")
    @Expose
    private var templateId: String? = null

    @SerializedName("templateName")
    @Expose
    private var templateName: String? = null

    @SerializedName("placeholders")
    @Expose
    private var placeholders: PlaceholdersModel? = null

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

    fun getFields(): FieldsModel? {
        return fields
    }

    fun setFields(fields: FieldsModel?) {
        this.fields = fields
    }

    fun getDatabaseName(): String? {
        return databaseName
    }

    fun setDatabaseName(databaseName: String?) {
        this.databaseName = databaseName
    }

    fun getDeviceId(): String? {
        return deviceId
    }

    fun setDeviceId(deviceId: String?) {
        this.deviceId = deviceId
    }

    fun getItemId(): String? {
        return itemId
    }

    fun setItemId(itemId: String?) {
        this.itemId = itemId
    }

    fun getItemLanguage(): String? {
        return itemLanguage
    }

    fun setItemLanguage(itemLanguage: String?) {
        this.itemLanguage = itemLanguage
    }

    fun getItemVersion(): Int? {
        return itemVersion
    }

    fun setItemVersion(itemVersion: Int?) {
        this.itemVersion = itemVersion
    }

    fun getLayoutId(): String? {
        return layoutId
    }

    fun setLayoutId(layoutId: String?) {
        this.layoutId = layoutId
    }

    fun getTemplateId(): String? {
        return templateId
    }

    fun setTemplateId(templateId: String?) {
        this.templateId = templateId
    }

    fun getTemplateName(): String? {
        return templateName
    }

    fun setTemplateName(templateName: String?) {
        this.templateName = templateName
    }

    fun getPlaceholders(): PlaceholdersModel? {
        return placeholders
    }

    fun setPlaceholders(placeholders: PlaceholdersModel?) {
        this.placeholders = placeholders
    }
}