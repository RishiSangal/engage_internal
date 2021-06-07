package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class SearchModel : Serializable {
    @SerializedName("Results")
    @Expose
    private var results: ResultsModel? = null

    fun getResults(): ResultsModel? {
        return results
    }

    fun setResults(results: ResultsModel?) {
        this.results = results
    }
}