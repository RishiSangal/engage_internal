package com.starkey.engage.ui.learn.data.learn

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