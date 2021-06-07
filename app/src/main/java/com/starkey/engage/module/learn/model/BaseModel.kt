package com.applications.engage.model

import org.json.JSONException
import org.json.JSONObject

abstract class BaseModel {
    abstract fun getJSON(): JSONObject?
    fun updateKey(key: String, value: Any?) {
        try {
            getJSON()?.put(key, value)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}