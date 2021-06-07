package com.starkey.engage.module.learn.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.starkey.engage.R
import com.starkey.engage.databinding.FragmentExperienceBinding
import com.starkey.engage.ui.learn.view.BaseFragment

class ExperienceFragment : BaseFragment(R.layout.fragment_experience) {

    lateinit var binding: FragmentExperienceBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExperienceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initArguments()
        this.initViews()
    }


    private fun initViews() {
        binding.layHeaderExperience.leftImageBtn.visibility = View.GONE
        binding.layHeaderExperience.txtCommonHeader.text = "Experience"
        binding.layHeaderExperience.rightImageBtn.visibility = View.VISIBLE
    }

//    var lessonModel: SearchResultLessonModel? = null
//    var nextLessonModel: SearchResultLessonModel? = null
    private fun initArguments() {
//        if (arguments != null && arguments?.containsKey(LESSON_OBJ) == true)
//            lessonModel = Gson().fromJson(arguments?.getString(LESSON_OBJ, "{}") ?: "{}", SearchResultLessonModel::class.java)
//        if (arguments != null && arguments?.containsKey(NEXT_LESSON_OBJ) == true)
//            nextLessonModel = Gson().fromJson(arguments?.getString(NEXT_LESSON_OBJ, "{}") ?: "{}", SearchResultLessonModel::class.java)
    }

    companion object {
        private const val LESSON_OBJ = "LESSON_OBJ"
        private const val NEXT_LESSON_OBJ = "NEXT_LESSON_OBJ"
        const val TAG = "TopicStartQuizFragment"
        fun newInstance(): ExperienceFragment {
            return ExperienceFragment()
        }
//        private fun getBundle(lessonModel: SearchResultLessonModel, nextLessonModel: SearchResultLessonModel? = null) = Bundle().apply {
//            putString(LESSON_OBJ, Gson().toJson(lessonModel))
//            nextLessonModel?.let {
//                putString(NEXT_LESSON_OBJ, Gson().toJson(nextLessonModel))
//            }
//        }
    }

}