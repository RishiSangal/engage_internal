package com.applications.engage.view.fragments

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.starkey.engage.ui.learn.data.learn.SearchResultLessonModel
import com.applications.engage.util.drawabletoolbox.DrawableBuilder
import com.starkey.engage.R
import com.starkey.engage.databinding.FragmentTopicItemSuccessBinding
import com.starkey.engage.ui.learn.utils.Utility
import com.starkey.engage.ui.learn.view.BaseFragment
import com.starkey.engage.module.learn.SupportTopicDetailActivity
import com.google.gson.Gson

class TopicItemSuccessFragment : BaseFragment(R.layout.fragment_topic_item_success) {

    lateinit var binding: FragmentTopicItemSuccessBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTopicItemSuccessBinding.inflate(inflater)
        return binding.root
    }

    val statusDrawable = DrawableBuilder().shape(GradientDrawable.OVAL).solidColor(
        Utility.getColor(
        R.color.color_primary)).build()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initArguments()
        this.initViews()
        this.setListenerOnWidgets()
    }

    private fun setListenerOnWidgets() {
    }

    private fun initViews() {
//        binding.tvSummery.text = Utility.fromHtml(topicTipModel?.getDescription()?.getValue().clean())
//        binding.tvTitle.text = Utility.fromHtml(topicTipModel?.getTitle()?.getValue().clean())
//        binding.tvNextTip.text = nextTopicTipModel?.title.clean()
        binding.tvSummery.text = "Now you know\nhow your hearing aids works"
        binding.imgSuccessStatus.background = statusDrawable
        binding.layBottomItemContainer.setOnClickListener( View.OnClickListener {
            (activity as SupportTopicDetailActivity).startExperienceScreen()
        })
    }

    var topicTipModel: SearchResultLessonModel? = null
    var nextTopicTipModel: SearchResultLessonModel? = null
    private fun initArguments() {
        if (arguments != null && arguments?.containsKey(LESSON_OBJ) == true)
            topicTipModel = Gson().fromJson(arguments?.getString(LESSON_OBJ, "{}") ?: "{}", SearchResultLessonModel::class.java)
        if (arguments != null && arguments?.containsKey(NEXT_LESSON_OBJ) == true)
            nextTopicTipModel = Gson().fromJson(arguments?.getString(NEXT_LESSON_OBJ, "{}") ?: "{}", SearchResultLessonModel::class.java)

    }

    companion object {
        private const val LESSON_OBJ = "LESSON_OBJ"
        private const val NEXT_LESSON_OBJ = "NEXT_LESSON_OBJ"

        const val TAG = "TopicItemTipFragment"
        fun newInstance(lessonModel: SearchResultLessonModel, nextLessonModel: SearchResultLessonModel?) = TopicItemSuccessFragment().apply {
            arguments = getBundle(lessonModel, nextLessonModel)
        }


        private fun getBundle(lessonModel: SearchResultLessonModel, nextLessonModel: SearchResultLessonModel?) = Bundle().apply {
            putString(LESSON_OBJ, Gson().toJson(lessonModel))
            nextLessonModel?.let {
                putString(NEXT_LESSON_OBJ, Gson().toJson(nextLessonModel))
            }
        }
    }


}