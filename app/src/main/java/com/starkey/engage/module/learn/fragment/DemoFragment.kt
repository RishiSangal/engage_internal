package com.applications.engage.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.starkey.engage.R
import com.starkey.engage.databinding.FragmentDemoBinding
import com.starkey.engage.ui.learn.view.BaseFragment

class DemoFragment : BaseFragment(R.layout.fragment_demo) {

    lateinit var binding: FragmentDemoBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDemoBinding.inflate(inflater)
//        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initViews()
        this.setListenerOnWidgets()
    }

    private fun setListenerOnWidgets() {
    }

    private fun initViews() {
        binding.layDemoHeader.leftImageBtn.visibility = View.GONE
        binding.layDemoHeader.txtCommonHeader.text = "Quick Lesson"
        binding.layDemoHeader.rightImageBtn.visibility = View.VISIBLE
    }


    companion object {
        fun newInstance(): DemoFragment {
            return DemoFragment()
        }


    }
}