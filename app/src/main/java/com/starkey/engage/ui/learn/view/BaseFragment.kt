package com.starkey.engage.ui.learn.view

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.starkey.engage.ui.learn.utils.ProgressBarHandler

abstract class BaseFragment(@LayoutRes id:Int) : Fragment(id) {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        progressBarHandler = ProgressBarHandler(requireActivity())
    }

/*    fun setupToolbar(backText: String? = null, title: String = "", commonToolBinding: CommonToolBinding) {
//        if (backText.isEmptyString())
//            commonToolBinding.tvBackTitle.makeGone()
//        else {
//            commonToolBinding.tvBackTitle.makeVisible()
//            commonToolBinding.tvBackTitle.text = backText
//        }
//        commonToolBinding.tootTitleTv.text = title
        commonToolBinding.leftImageBtn.setOnClickListener { activity?.onBackPressed() }
//        commonToolBinding.tvBackTitle.setForegroundRipple()
//        commonToolBinding.tvBackTitle.setOnClickListener { activity?.onBackPressed() }
        commonToolBinding.leftImageBtn.setForegroundRipple()
    }*/

    private lateinit var progressBarHandler: ProgressBarHandler
    fun showLoader() {
        progressBarHandler.show()
    }

    fun hideLoader() {
        progressBarHandler.hide()
    }

    /*    protected fun getPageChangeCallback(): ChangePageCallback? {
        return when {
            parentFragment is ChangePageCallback -> parentFragment as ChangePageCallback
            context is ChangePageCallback -> context as ChangePageCallback
            else -> null
        }
    }

    fun showToast(message: String?) {
        activity?.let {
            if (it is BaseActivity)
                it.showToast(message)
        }
    }*/

}