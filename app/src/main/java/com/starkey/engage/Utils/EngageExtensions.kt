@file:Suppress("DEPRECATION")

package com.starkey.engage.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.*
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import com.applications.engage.util.drawabletoolbox.DrawableBuilder
import com.starkey.engage.R
import com.starkey.engage.ui.learn.utils.Utility
import java.text.DecimalFormat

internal fun Char.isPlaceHolder(): Boolean = this == '#'

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}


fun String?.parseInt(default: Int = 0): Int {
    return try {
        if (this == null || this.isEmpty()) {
            default
        } else toInt()
    } catch (e: NumberFormatException) {
        default
    }
}

fun String?.parseLong(default: Long = 0): Long {
    return try {
        if (this == null || this.isEmpty()) {
            default
        } else toLong()
    } catch (e: NumberFormatException) {
        default
    }
}

fun String?.parseBoolean(default: Boolean = false): Boolean {
    return try {
        if (this == null || this.isEmpty()) {
            default
        } else toBoolean()
    } catch (e: NumberFormatException) {
        default
    }
}

fun String?.parseFloat(default: Float = 0.0f): Float {
    return try {
        if (this == null || this.isEmpty()) {
            default
        } else toFloat()
    } catch (e: NumberFormatException) {
        default
    }
}

fun String?.parseDouble(default: Double = 0.0): Double {
    return try {
        if (this == null || this.isEmpty()) {
            default
        } else toDouble()
    } catch (e: NumberFormatException) {
        default
    }
}

fun String?.parseString(default: String = ""): String {
    return try {
        if (this != null && !this.equals("null", ignoreCase = true)) {
            this
        } else default
    } catch (e: NumberFormatException) {
        default
    }
}

fun String.handleNewLine(): String {
    return this.replace("<br/>", "\n")
}

fun Int.dp2Px(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun Int.sp2Px(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun String?.clean(): String {
    return if (this?.isEmptyString() != false) "" else this.trim()
}

fun String?.toDecimalPlaces(decimalPlaces: Int): String {
    if (this.isEmptyString()) return "0".toDecimalPlaces(decimalPlaces)
    val decimalFormat = DecimalFormat("#0.00")
    decimalFormat.maximumFractionDigits = decimalPlaces
    decimalFormat.minimumFractionDigits = decimalPlaces
    val doubleValue: Double
    try {
        doubleValue = this!!.toDouble()
    } catch (numberFormatException: java.lang.NumberFormatException) {
        return this + ""
    }
    return decimalFormat.format(doubleValue)
}

fun String?.isEmptyString(): Boolean {
    return !this.isNonEmptyString()
}

fun String?.isNonEmptyString(): Boolean {
    return (this != null && !this.equals("null", ignoreCase = true) && this.trim().isNotEmpty())
}

/**
 * Visibility modifiers and check functions
 */

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.toggleVisibility() {
    if (this.visibility == View.VISIBLE)
        this.visibility = View.GONE
    else
        this.visibility = View.VISIBLE
}

/**
 * Hides the Soft Input Keyboard from the screen
 */
fun View.hideKeyboard() {
    val inputMethodManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.hideKeyboard() {
//    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//    val focusedView = currentFocus
//    if (focusedView != null)
//        inputMethodManager?.hideSoftInputFromWindow(focusedView.windowToken, 0)
    currentFocus?.hideKeyboard()
}

fun View.showKeyboard(context: Context?) {
    val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}


fun View.setForegroundRipple() {
    this.foreground = DrawableBuilder()
        .ripple()
        .rippleColor(Utility.adjustAlpha(Utility.getColor(R.color.purple), 0.2f))
        .build()
}

fun View.setBackgroundRipple() {
    this.background = DrawableBuilder()
        .ripple()
        .rippleColor(Utility.adjustAlpha(Utility.getColor(R.color.purple), 0.2f))
        .build()
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.enableNestedScroll() {
    this.setOnTouchListener { v, event ->
        if (this.hasFocus()) {
            v.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_SCROLL -> {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                    return@setOnTouchListener true
                }
            }
        }
        return@setOnTouchListener false
    }
}

fun Context.isInternetAvailable(): Boolean {
    var result = false
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }

    return result
}

fun String?.getCleanedString(): String {
    return if (this.isEmptyString()) "" else this!!
}

fun Double.format(decimalDigits: Int = 2): String {
    val format = DecimalFormat()
    format.maximumFractionDigits = decimalDigits
    format.minimumFractionDigits = decimalDigits
    return format.format(this)
}

fun Float.format(decimalDigits: Int = 2): String {
    val format = DecimalFormat()
    format.maximumFractionDigits = decimalDigits
    format.minimumFractionDigits = decimalDigits
    return format.format(this)
}

fun TextView.makeUnderLine() {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun String.getClickableSpan(
    toSpan: String,
    @ColorInt color: Int = 0,
    isHavingUnderline: Boolean = true,
    shouldBeBold: Boolean = false,
    clickEvent: (View) -> Unit
): SpannableString {
    val signUpSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            widget.cancelPendingInputEvents()
            clickEvent.invoke(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            if (!isHavingUnderline) {
                ds.isUnderlineText = false
            }

            if (shouldBeBold) {
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }
    }

    val spanStartIndex = this.indexOf(toSpan)
    val spanEndIndex = spanStartIndex + toSpan.length
    val span = SpannableString(this)
    span.setSpan(
        signUpSpan,
        spanStartIndex,
        spanEndIndex,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    if (color != 0) {
        span.setSpan(
            ForegroundColorSpan(color),
            spanStartIndex,
            spanEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    return span
}


