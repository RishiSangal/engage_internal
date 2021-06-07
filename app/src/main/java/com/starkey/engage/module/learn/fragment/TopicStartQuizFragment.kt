//package com.example.engageandroid.module.learn.fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.viewpager.widget.ViewPager
//import com.starkey.engage.ui.learn.data.learn.SearchResultLessonModel
//import com.applications.engage.model.search.ItemModel1
//import com.example.engageandroid.R
//import com.example.engageandroid.Utils.SharedPrefUtils
//import com.example.engageandroid.Utils.clean
//import com.example.engageandroid.databinding.FragmentTopicStartQuizBinding
//import com.example.engageandroid.module.`interface`.ChangePageCallback
//import com.example.engageandroid.module.learn.TopicStartQuizFragmentAdapter
//import com.example.engageandroid.module.learn.UserGuideSubListAdapter
//import com.google.gson.Gson
//import com.example.engageandroid.module.learn.BaseFragment
//
//class TopicStartQuizFragment : BaseFragment(R.layout.fragment_topic_start_quiz), UserGuideSubListAdapter.OnGuideClick,
//    ChangePageCallback {
//
//    lateinit var binding: FragmentTopicStartQuizBinding
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        binding = FragmentTopicStartQuizBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        this.initArguments()
//        this.initViews()
//        this.setupAdapter()
//    }
//
//    private fun setupAdapter() {
//        lessonModel?.let {
//            binding.viewPager.adapter = TopicStartQuizFragmentAdapter(childFragmentManager, it, nextLessonModel)
//            SharedPrefUtils.setProgress(lessonModel?.uniqueID.clean(), ((1f / (binding.pageIndicatorView.count - 1).toFloat()) * 100).toInt())
//            val progressPct = SharedPrefUtils.getCurrentProgress(lessonModel?.uniqueID.clean())
//            binding.viewPager.currentItem = ((progressPct.toFloat() / 100f) * binding.pageIndicatorView.count.toFloat()).toInt() - 1
//            binding.viewPager.offscreenPageLimit = (binding.viewPager.adapter as TopicStartQuizFragmentAdapter).count
//        }
//    }
//
//    private fun initViews() {
//        /*
//        pageIndicatorView.setCount(5); // specify total count of indicators
//        pageIndicatorView.setSelection(2);
//         */
//        val quizCount = lessonModel?.quizes?.size ?: 0
//        val tipsCount = lessonModel?.articles?.size ?: 0
//
//        binding.pageIndicatorView.count = quizCount + tipsCount + 1
//        binding.pageIndicatorView.selection = 0
//        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
//
//            override fun onPageSelected(position: Int) {
//                binding.pageIndicatorView.selection = position
//                SharedPrefUtils.setProgress(lessonModel?.uniqueID.clean(), (((position + 1).toFloat() / (binding.pageIndicatorView.count - 1).toFloat()) * 100).toInt())
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {}
//
//        })
//    }
//
//    var lessonModel: SearchResultLessonModel? = null
//    var nextLessonModel: SearchResultLessonModel? = null
//    private fun initArguments() {
//        if (arguments != null && arguments?.containsKey(LESSON_OBJ) == true)
//            lessonModel = Gson().fromJson(arguments?.getString(LESSON_OBJ, "{}") ?: "{}", SearchResultLessonModel::class.java)
//        if (arguments != null && arguments?.containsKey(NEXT_LESSON_OBJ) == true)
//            nextLessonModel = Gson().fromJson(arguments?.getString(NEXT_LESSON_OBJ, "{}") ?: "{}", SearchResultLessonModel::class.java)
//    }
//
//    companion object {
//        private const val LESSON_OBJ = "LESSON_OBJ"
//        private const val NEXT_LESSON_OBJ = "NEXT_LESSON_OBJ"
//        const val TAG = "TopicStartQuizFragment"
//        fun newInstance(lessonModel: SearchResultLessonModel, nextLessonModel: SearchResultLessonModel? = null) = TopicStartQuizFragment().apply {
//            arguments = getBundle(lessonModel, nextLessonModel)
//        }
//        private fun getBundle(lessonModel: SearchResultLessonModel, nextLessonModel: SearchResultLessonModel? = null) = Bundle().apply {
//            putString(LESSON_OBJ, Gson().toJson(lessonModel))
//            nextLessonModel?.let {
//                putString(NEXT_LESSON_OBJ, Gson().toJson(nextLessonModel))
//            }
//        }
//    }
//
//    override fun onGuideClick(itemModel: ItemModel1?) {
//
//    }
//
//    override fun showNextPage() {
//        binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
//    }
//
//    override fun showPreviousPage() {
//        binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
//    }
//}