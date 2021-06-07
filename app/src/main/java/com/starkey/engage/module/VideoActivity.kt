//package com.starkey.engage.module
//
//import android.os.Bundle
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.brightcove.player.edge.Catalog
//import com.brightcove.player.edge.CatalogError
//import com.brightcove.player.edge.VideoListener
//import com.brightcove.player.mediacontroller.BrightcoveMediaController
//import com.brightcove.player.model.Video
//import com.brightcove.player.view.BaseVideoView
//import com.brightcove.player.view.BrightcoveExoPlayerVideoView
//import com.starkey.engage.R
//import com.starkey.engage.ui.learn.utils.ProgressBarHandler
//import com.starkey.engage.Utils.SharedPrefUtils
//import com.starkey.engage.Utils.clean
//import com.starkey.engage.Utils.parseLong
//import com.starkey.engage.ui.learn.utils.Utility
//import com.starkey.engage.ui.learn.viewmodel.LearnViewModel
//import kotlinx.android.synthetic.main.activity_video.*
//import org.json.JSONObject
//
//class VideoActivity : AppCompatActivity(), VideosAdapter.OnVideoClick{
////    lateinit var brightcoveVideoExoView: BrightcoveExoPlayerVideoView
//
//    //    lateinit var analytics: Analytics
////    private lateinit var recyclerview_videos: RecyclerView
//    private lateinit var videosAdapter: VideosAdapter
//    var videoModel: SearchResultVideoModel? = null
//    var brightcoveVideoView : BrightcoveExoPlayerVideoView? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        setContentView(R.layout.activity_video)
//        brightcoveVideoView = findViewById(R.id.brightcove_video_view);
//        super.onCreate(savedInstanceState)
//        progressBarHandler = ProgressBarHandler(this)
//        initArguments()
////        initMediaController(brightcoveVideoView)
////        brightcoveVideoView.eventEmitter.on(EventType.PROGRESS) { event ->
////            val currentProgress = event?.properties?.get("playheadPosition")?.toString().clean().parseInt()
////            SharedPrefUtils.setProgress(videoModel?.videoID.clean(), currentProgress)
////        }
//        getVideoData()
//    }
//
//    private var viewModel: LearnViewModel? = null
//    private fun getVideoData() {
//        showLoader()
//        viewModel = ViewModelProvider(this).get(LearnViewModel::class.java)
//        viewModel!!.detailVideoLiveData.observe(this, { apiResponse ->
//
//            hideLoader()
//            var wholedata = JSONObject(apiResponse)
//            wholedata = wholedata.optJSONObject("Model") ?: JSONObject()
//            videoModel = SearchResultVideoModel.mapWithJson(wholedata)
//            startVideo()
//            updateUI()
//        })
//        viewModel!!.getVideoDetail(searchContentId)
////        SupportRepository(this).getSearchResultDetailData(NetworkAPIConstants.Tag.GET_SEARCH_RESULT_VIDEO_DETAIL_DATA, searchContentId)
//    }
//
//    private fun startVideo() {
//        val eventEmitter = brightcoveVideoView?.eventEmitter
//        val catalog = eventEmitter?.let { Catalog(it, getString(R.string.account), getString(R.string.policy)) }
//        catalog?.findVideoByID(videoModel?.videoID.clean(), object : VideoListener() {
//            override fun onVideo(video: Video?) {
//                txtVideoDuration.text = Utility.convertToUserFriendlyTime(video?.duration.toString().parseLong())
//                brightcoveVideoView?.add(video)
//                brightcoveVideoView?.start()
//                brightcoveVideoView?.seekTo(SharedPrefUtils.getProgress(videoModel?.videoID.clean()))
//            }
//
//            override fun onError(errors: MutableList<CatalogError>) {
//                super.onError(errors)
//            }
//        })
//    }
//
//    lateinit var videTitle: TextView
//    lateinit var videoDesc: TextView
//    lateinit var leftImageBtn: ImageView
//    lateinit var txtVideoDuration: TextView
//
//    //    var videoId = ""
//    var searchContentId = ""
//    private fun updateUI() {
//        videTitle = findViewById(R.id.videoTitle)
//        videoDesc = findViewById(R.id.videoDesc)
//        txtVideoDuration = findViewById(R.id.txtVideoDuration)
//        leftImageBtn = findViewById(R.id.leftImageBtn)
//        leftImageBtn.setOnClickListener { onBackPressed() }
//        videoModel?.let { videoModel ->
//            videTitle.text = videoModel.title
//            videoDesc.text = Utility.fromHtml(videoModel.description)
////            txtVideoDuration.text = Utility.convertToUserFriendlyTime(videoModel.duration.parseLong())
//        }
//
//        val exploreMore = ArrayList<PopularItems>()
//        exploreMore.add(PopularItems(R.drawable.cta_1, "Adjusting the sound"))
//        exploreMore.add(PopularItems(R.drawable.cta_1, "How to insert hearing aids"))
//        exploreMore.add(PopularItems(R.drawable.cta_1, "Cleaning tips"))
//
////        recyclerview_videos = findViewById(R.id.recyclerview_videos)
//        recyclerview_videos.layoutManager = LinearLayoutManager(this,
//            LinearLayoutManager.HORIZONTAL, false)
//        videosAdapter = VideosAdapter(this, this, exploreMore)
//        recyclerview_videos.adapter = videosAdapter
//
//    }
//
//    override fun onVideoClick(videoModel: VideoModel?) {
//
//    }
//
//    open fun initMediaController(brightcoveVideoView: BaseVideoView) {
//        brightcoveVideoView.setMediaController(BrightcoveMediaController(brightcoveVideoView, R.layout.brightcove_control_bar))
//    }
//
//    private fun initArguments() {
//        if (intent.extras != null && intent?.hasExtra(SEARCH_CONTENT_ID) == true)
//            searchContentId = intent.extras?.getString(SEARCH_CONTENT_ID, "").clean()
//    }
//
//    companion object {
//        private const val SEARCH_CONTENT_ID = "SEARCH_CONTENT_ID"
//        fun getBundle(videoId: String) = Bundle().apply {
//            putString(SEARCH_CONTENT_ID, videoId)
//        }
//    }
//
////    override fun onAPIResponse(requestTag: String?, data: AppData<Any>?) {
////        hideLoader()
////        when (requestTag) {
////            NetworkAPIConstants.Tag.GET_SEARCH_RESULT_VIDEO_DETAIL_DATA -> {
////                if (data is AppData.Success) {
////                    this.videoModel = data.data as SearchResultVideoModel
////                    startVideo()
////                }
////            }
////        }
////    }
//
//    lateinit var progressBarHandler: ProgressBarHandler
//    private fun showLoader() {
//        progressBarHandler.show()
//    }
//
//    private fun hideLoader() {
//        progressBarHandler.hide()
//    }
//
//
//}