package com.starkey.engage.ui.learn.utils

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.starkey.engage.EngageApplication
import com.starkey.engage.Utils.isEmptyString
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


class Utility {
    companion object {

        inline fun <reified T> convertStringToObject(jsonString: String?): T? {
            return try {
                if (jsonString.isEmptyString()) {
                    null
                } else {
                    Gson().fromJson(jsonString, T::class.java)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun encryptValue(value: String?): String? {
            try {
                return URLEncoder.encode(value, "UTF-8")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return value
        }

        fun getCountryLocale(): String =
            if (Locale.getDefault().language.equals("en", ignoreCase = true)) Locale.getDefault().language else "${Locale.getDefault().language}-${Locale.getDefault().country}"

        fun fromHtml(html: String?): Spanned? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(html)
            }
        }

        fun convertToUserFriendlyTime(milliseconds: Long): String {
            val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(milliseconds))
            val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(milliseconds))
            var time = if (hours.toInt() == 0) "" else "$hours ${if (hours == 1L) "hour " else "hours "}"
            time += if (minutes.toInt() == 0) "" else "$minutes ${if (minutes == 1L) "minute " else "minutes"} "
            time += if (seconds.toInt() == 0) "" else "$seconds ${if (seconds == 1L) "second" else "seconds"}"
            return time
        }

        fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
            val alpha = (Color.alpha(color) * factor).roundToInt()
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)
            return Color.argb(alpha, red, green, blue)
        }

        fun getColor(@ColorRes colorResId: Int): Int {
            return ContextCompat.getColor(EngageApplication.getGlobalAppContext(), colorResId)
        }

        fun addOrReplaceFragment(
            fragmentManager: FragmentManager,
            containerId: Int,
            fragment: Fragment,
            tag: String,
            isAddFragment: Boolean = false,
            isAddBackStack: Boolean = false
        ) {
            val transaction = fragmentManager.beginTransaction()
            if (isAddFragment) {
                transaction.add(containerId, fragment, tag)
            } else {
                transaction.replace(containerId, fragment, tag)
            }
            if (fragmentManager.fragments.isNotEmpty() && isAddBackStack) {
                transaction.addToBackStack(tag)
            }
            transaction.commit()
        }
    }
}
