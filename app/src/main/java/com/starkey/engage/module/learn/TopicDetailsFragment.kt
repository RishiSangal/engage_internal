package com.starkey.engage.module.learn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.starkey.engage.ui.learn.data.learn.SearchResultLessonModel
import com.applications.engage.model.search.ItemModel1
import com.starkey.engage.R
import com.starkey.engage.Utils.clean
import com.starkey.engage.databinding.FragmentTopicDetailsBinding
import com.starkey.engage.ui.learn.utils.Utility
import com.google.gson.Gson
import com.starkey.engage.ui.learn.view.BaseFragment

class TopicDetailsFragment : BaseFragment(R.layout.fragment_topic_details), UserGuideSubListAdapter.OnGuideClick {
    interface OnStartTopicClickListener {
        fun onStartTopicClick(lessonModel: SearchResultLessonModel)
    }

    lateinit var binding: FragmentTopicDetailsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTopicDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initArguments()
        this.initViews()
        this.setListenerOnWidgets()
    }

    private fun setListenerOnWidgets() {
        binding.btnStart.setOnClickListener {
            lessonModel?.let {
                activity?.let { activity ->
                    if (activity is OnStartTopicClickListener)
                        activity.onStartTopicClick(it)
                }
            }
        }
    }

    private fun initViews() {
        binding.layTopicHeader.rightImageBtn.visibility = View.VISIBLE
        binding.layTopicHeader.txtCommonHeader.text = "Quick Lesson"
        binding.imgTopicHeader.setImageResource(R.drawable.ear_bud)

        binding.txtQuickHeading.text = lessonModel!!.title
        binding.txtQuickSubHeading.text = Utility.fromHtml(lessonModel?.description.clean())
        //        binding.tvSummery.text = Utility.fromHtml(lessonModel?.description.clean())
//        binding.tvTitle.text = lessonModel?.title.clean()
//        val quizCount = lessonModel?.quizes?.size ?: 0
//        val tipsCount = lessonModel?.articles?.size ?: 0
//        var tipsInfo = ""
//        if (tipsCount > 0)
//            tipsInfo = "$tipsCount tips"
//        if (tipsCount > 0 && quizCount > 0)
//            tipsInfo += " and"
//        if (quizCount > 0) {
//            if (quizCount == 1)
//                tipsInfo += " a quiz"
//            else
//                tipsInfo += " $quizCount quizzes"
//        }
//        binding.tvTipsInfo.text = tipsInfo
    }

    var lessonModel: SearchResultLessonModel? = null
    private fun initArguments() {
        if (arguments != null && arguments?.containsKey(LESSON_OBJ) == true)
            lessonModel = Gson().fromJson(arguments?.getString(LESSON_OBJ, "{}") ?: "{}", SearchResultLessonModel::class.java)
    }


    companion object {
        private const val LESSON_OBJ = "LESSON_OBJ"
        const val TAG = "TopicDetailsFragment"
        fun newInstance(lessonModel: SearchResultLessonModel) = TopicDetailsFragment().apply {
            arguments = getBundle(lessonModel)
        }


        private fun getBundle(lessonModel: SearchResultLessonModel) = Bundle().apply {
            putString(LESSON_OBJ, Gson().toJson(lessonModel))
        }
    }

    override fun onGuideClick(itemModel: ItemModel1?) {

    }
}