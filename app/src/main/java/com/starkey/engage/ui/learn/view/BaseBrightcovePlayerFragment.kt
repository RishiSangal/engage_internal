package com.starkey.engage.ui.learn.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.brightcove.player.event.EventLogger
import com.brightcove.player.mediacontroller.BrightcoveMediaController
import com.brightcove.player.util.LifecycleUtil
import com.brightcove.player.view.BaseVideoView
import com.brightcove.player.view.BrightcoveVideoView
import com.starkey.engage.R


open class BaseBrightcovePlayerFragment(@LayoutRes id:Int) : BaseFragment(id) {
    var baseVideoView: BaseVideoView? = null
        protected set
    private var lifecycleUtil: LifecycleUtil? = null
    private var eventLogger: EventLogger? = null
    fun getBrightcoveVideoView(): BrightcoveVideoView? {
        var result: BrightcoveVideoView? = null
        if (baseVideoView is BrightcoveVideoView) {
            result = baseVideoView as BrightcoveVideoView?
        }
        return result
    }

    open fun initMediaController(brightcoveVideoView: BaseVideoView) {
        brightcoveVideoView.setMediaController(BrightcoveMediaController(brightcoveVideoView, R.layout.brightcove_control_bar))
    }

    fun fullScreen() {
        if (!baseVideoView!!.isFullScreen) {
            baseVideoView!!.eventEmitter.emit("enterFullScreen")
        } else {
            Log.e(TAG, "Event emitter is not defined or the video view is already in full screen mode.")
        }
    }

    fun normalScreen() {
        if (baseVideoView!!.isFullScreen) {
            baseVideoView!!.eventEmitter.emit("exitFullScreen")
        } else {
            Log.e(TAG, "Event emitter is not defined or the video view is not in full screen mode!")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.v(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        lifecycleUtil!!.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.v(TAG, "onCreateView")
        val view = super.onCreateView(inflater, container, savedInstanceState)
        return if (baseVideoView != null) {
            baseVideoView?.let { baseVideoView ->
                lifecycleUtil = LifecycleUtil(baseVideoView)
                lifecycleUtil!!.onCreateView(savedInstanceState, this)
                eventLogger = EventLogger(baseVideoView.eventEmitter, true, this.javaClass.simpleName)
                initMediaController(baseVideoView)
            }
            view
        } else {
            throw IllegalStateException("brightcoveVideoView must be assigned before calling onCreateView().")
        }
    }

    override fun onStart() {
        Log.v(TAG, "onStart")
        super.onStart()
        lifecycleUtil!!.fragmentOnStart()
    }

    override fun onPause() {
        Log.v(TAG, "onPause")
        super.onPause()
        lifecycleUtil!!.fragmentOnPause()
    }

    override fun onResume() {
        Log.v(TAG, "onResume")
        super.onResume()
        baseVideoView!!.eventEmitter.on("changeOrientation") { event ->
            val orientation = event.getIntegerProperty("requestedOrientation")
            this@BaseBrightcovePlayerFragment.activity?.requestedOrientation = orientation
        }
        lifecycleUtil!!.fragmentOnResume()
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        super.onDestroy()
        lifecycleUtil!!.fragmentOnDestroy()
    }

    override fun onDestroyView() {
        Log.v(TAG, "onDestroyView")
        super.onDestroyView()
        lifecycleUtil!!.onDestroyView()
    }

    override fun onDetach() {
        Log.v(TAG, "onDetach")
        super.onDetach()
        lifecycleUtil!!.onDetach()
    }

    override fun onStop() {
        Log.v(TAG, "onStop")
        super.onStop()
        lifecycleUtil!!.fragmentOnStop()
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        Log.v(TAG, "onSaveInstanceState")
        baseVideoView!!.eventEmitter.on("fragmentSaveInstanceState") { super.onSaveInstanceState(bundle) }
        lifecycleUtil!!.fragmentOnSaveInstanceState(bundle)
    }

//    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
//        super.onPictureInPictureModeChanged(isInPictureInPictureMode)
//        PictureInPictureManager.getInstance().onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
//    }

    companion object {
        val TAG = BaseBrightcovePlayerFragment::class.java.simpleName
    }
}