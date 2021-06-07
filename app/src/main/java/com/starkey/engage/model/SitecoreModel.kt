package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class SitecoreModel : Serializable {
    @SerializedName("context")
    @Expose
    private var context: ContextModel? = null

    @SerializedName("route")
    @Expose
    private var route: RouteModel? = null

    fun getContext(): ContextModel? {
        return context
    }

    fun setContext(context: ContextModel?) {
        this.context = context
    }

    fun getRoute(): RouteModel? {
        return route
    }

    fun setRoute(route: RouteModel?) {
        this.route = route
    }
}
