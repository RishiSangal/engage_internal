package com.starkey.engage.module.learn

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.applications.engage.view.fragments.*
import com.starkey.engage.ui.learn.data.learn.SearchResultLessonModel
import com.starkey.engage.ui.learn.data.learn.SearchResultQuizModel

open class TopicStartQuizFragmentAdapter(
    fragmentManager: FragmentManager,
    private val lessonModel: SearchResultLessonModel,
    private val nextLessonModel: SearchResultLessonModel?
) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val items = ArrayList<Any>()

    init {
        items.clear()
        items.addAll(lessonModel.articles)
        items.addAll(lessonModel.quizes)
        items.addAll(lessonModel.videos)
        items.add(SearchResultQuizModel())
        items.add("SUCCESS_PAGE")
    }

    override fun getCount() = items.size

    override fun getItem(position: Int): Fragment {
        items[position].let {
            return when (it) {
//                is Int -> DemoFragment.newInstance()
//                is SearchResultArticleModel -> TopicItemTipFragment.newInstance(it)
//                is SearchResultQuizModel -> TopicItemQuizFragment.newInstance()
//                is SearchResultVideoModel -> TopicItemVideoFragment.newInstance(it)
//                else -> TopicItemSuccessFragment.newInstance()

//                is SearchResultArticleModel -> TopicItemTipFragment.newInstance(it)
//                is SearchResultQuizModel -> TopicItemQuizFragment.newInstance(it)
//                is SearchResultVideoModel -> TopicItemVideoFragment.newInstance(it)
                else -> TopicItemSuccessFragment.newInstance(lessonModel, nextLessonModel)
            }
        }

    }

}