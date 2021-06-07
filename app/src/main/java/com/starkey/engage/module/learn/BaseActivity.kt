package com.starkey.engage.module.learn

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.starkey.engage.EngageApplication
import com.starkey.engage.ui.learn.utils.ProgressBarHandler
import com.starkey.engage.Utils.SLog

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getViewBinder(inflater = layoutInflater).root)
        progressBarHandler = ProgressBarHandler(this)
    }

    override fun onResume() {
        super.onResume()
        SLog.i("Current_Activity:", "(" + javaClass.simpleName + ".kt:0)")
    }

    abstract fun getViewBinder(inflater: LayoutInflater): ViewBinding

    var toast: Toast? = null

    @SuppressLint("ShowToast")
    open fun showToast(message: String?) {
        if (TextUtils.isEmpty(message)) return
        Handler(Looper.getMainLooper()).post {
            if (toast == null) {
                toast = Toast.makeText(EngageApplication.getGlobalAppContext(), message, Toast.LENGTH_SHORT)
            }
            toast?.setText(message)
            toast?.show()
        }
    }
    lateinit var progressBarHandler: ProgressBarHandler
    fun showLoader() {
        progressBarHandler.show()
    }

    fun hideLoader() {
        progressBarHandler.hide()
    }
}