package com.applications.engage.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ResultsModel : Serializable {
    @SerializedName("videos")
    @Expose
    private var videos: List<VideoModel?>? = null

    @SerializedName("topics")
    @Expose
    private var topics: List<TopicModel?>? = null

    fun getVideos(): List<VideoModel?>? {
        return videos
    }

    fun setVideos(videos: List<VideoModel?>?) {
        this.videos = videos
    }

    fun getTopics(): List<TopicModel?>? {
        return topics
    }

    fun setTopics(topics: List<TopicModel?>?) {
        this.topics = topics
    }

}