package com.applications.engage.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.starkey.engage.ui.learn.data.learn.SearchResultQuizModel
import com.starkey.engage.R
import com.starkey.engage.databinding.FragmentTopicItemQuizBinding
import com.starkey.engage.ui.learn.view.BaseFragment
import com.google.gson.Gson
import com.starkey.engage.Utils.makeVisible

class TopicItemQuizFragment : BaseFragment(R.layout.fragment_topic_item_quiz) {

    lateinit var binding: FragmentTopicItemQuizBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTopicItemQuizBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initArguments()
        this.initViews()
        this.setListenerOnWidgets()
    }

    private fun setListenerOnWidgets() {
        binding.btnGotIt.setOnClickListener {
//            getPageChangeCallback()?.showNextPage()
        }
//        binding.tvSkipQuiz.setOnClickListener {
//            getPageChangeCallback()?.showNextPage()
//        }
//        binding.tvSkipQuiz.setForegroundRipple()
    }

    private fun initViews() {
        binding.layHeaderTopicQuiz.leftImageBtn.visibility = View.GONE
        binding.layHeaderTopicQuiz.rightImageBtn.visibility = View.VISIBLE
        binding.layHeaderTopicQuiz.txtCommonHeader.text = "Quick lesson"
//        binding.tvTitle.text = Utility.fromHtml(quizModel?.title?.clean())
//        showAnswers()
//        updateUI()
    }

    private fun showAnswers() {
//        binding.layOptionPlaceHolder.removeAllViews()
//        this@TopicItemQuizFragment.binding.tvSubTitle.text = ""
//        activity?.let { activity ->
//            quizModel?.quizOptions?.forEach {
//                it?.let { currentAnswer ->
//                    val convertView = LayoutInflater.from(activity).inflate(R.layout.quiz_option_item_view, null)
//                    val binding = QuizOptionItemViewBinding.bind(convertView)
//                    binding.tvOptionTitle.text = currentAnswer.title.clean()
//                    if (currentAnswerState == STATE_ANSWERED) {
//                        val correctAnswerId = quizModel?.getCorrectAnswer()?.uniqueID.clean()
//                        val selectedAnswerId = selectedAnswer?.uniqueID.clean()
//                        val currentAnswerId = currentAnswer.uniqueID.clean()
//                        val isCorrectAnswerSelected = correctAnswerId.contains(selectedAnswerId, ignoreCase = true)
//                        val isCurrentAnswerCorrectAnswer = correctAnswerId.contains(currentAnswerId, ignoreCase = true)
//                        if (isCurrentAnswerCorrectAnswer) {
//                            binding.imgAnswerStatus.makeVisible()
//                            binding.imgAnswerStatus.setImageResource(R.drawable.quiz_correct)
//                            this@TopicItemQuizFragment.binding.tvSubTitle.text = Utility.fromHtml(quizModel?.getCorrectAnswer()?.subTitle)
//                        } else if (selectedAnswerId.equals(currentAnswerId, ignoreCase = true)) {
//                            binding.imgAnswerStatus.makeVisible()
//                            binding.imgAnswerStatus.setImageResource(R.drawable.quiz_wrong)
//                        }
//                        if (selectedAnswerId.equals(currentAnswerId, ignoreCase = true))
//                            binding.itemContainer.isSelected = true
//                    }
//                    binding.root.setOnClickListener {
//                        if (currentAnswerState == STATE_ANSWERED)
//                            return@setOnClickListener
//                        currentAnswerState = STATE_ANSWERED
//                        selectedAnswer = currentAnswer
//                        showAnswers()
//                        updateUI()
//                    }
//                    this.binding.layOptionPlaceHolder.addView(convertView)
//
//                }
//            }
//        }
    }

    private fun updateUI() {
//        binding.tvSkipQuiz.makeVisible()
        binding.btnGotIt.makeVisible()
//        if (currentAnswerState == STATE_ANSWERED) {
//            binding.tvSkipQuiz.makeGone()
//        } else
//            binding.btnGotIt.makeGone()
    }

//    var selectedAnswer: LessonQuizAnswerModel? = null
//    var quizModel: SearchResultQuizModel? = null
//    var currentAnswerState = STATE_NOT_ANSWERED
    private fun initArguments() {
//        if (arguments != null && arguments?.containsKey(QUIZ_ITEM_MODEL_OBJ) == true)
//            quizModel = Gson().fromJson(arguments?.getString(QUIZ_ITEM_MODEL_OBJ, "{}") ?: "{}", SearchResultQuizModel::class.java)
    }

    companion object {
        private const val QUIZ_ITEM_MODEL_OBJ = "QUIZ_ITEM_MODEL_OBJ"
        const val STATE_SKIPPED = 0
        const val STATE_ANSWERED = 1
        const val STATE_NOT_ANSWERED = 3
        const val TAG = "TopicItemQuizFragment"
        fun newInstance(quizModel: SearchResultQuizModel) = TopicItemQuizFragment().apply {
            arguments = getBundle(quizModel)
        }


        private fun getBundle(quizModel: SearchResultQuizModel) = Bundle().apply {
            putString(QUIZ_ITEM_MODEL_OBJ, Gson().toJson(quizModel))
        }
    }

}