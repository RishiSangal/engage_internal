package com.starkey.engage.Utils

import io.paperdb.Paper

object SharedPrefUtils {
    val DB_PROGRESS = "DB_PROGRESS"
    fun setProgress(id: String, updateProgress: Int) {
        val currentProgress = getProgress(id)
        if (currentProgress < updateProgress)
            Paper.book(DB_PROGRESS).write(id, updateProgress)
        Paper.book(DB_PROGRESS).write(id + "_current", updateProgress)
    }

    /**
     * this is the max progress user achieved for an id*/
    fun getProgress(id: String) = Paper.book(DB_PROGRESS).read(id, 0)

    /**
     * this is the last progress user left for an id*/
    fun getCurrentProgress(id: String) = Paper.book(DB_PROGRESS).read(id + "_current", 0)
}