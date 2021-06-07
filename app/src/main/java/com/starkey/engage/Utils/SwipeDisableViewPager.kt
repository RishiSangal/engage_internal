package com.starkey.engage.Utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class SwipeDisableViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
    enum class SwipeDirection { all, none, left, right }

    init {
        addOnPageChangeListener(object : OnPageChangeListener {
            var lastPageSelected = 0
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (direction != SwipeDirection.all) {
                    if (lastPageSelected > position)
                        this@SwipeDisableViewPager.currentItem = lastPageSelected
                    else
                        lastPageSelected = position
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private var initialXValue = 0f
    private var direction: SwipeDirection? = SwipeDirection.all

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isSwipeAllowed(event)) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (isSwipeAllowed(event)) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    private fun isSwipeAllowed(event: MotionEvent): Boolean {
        if (direction === SwipeDirection.all) return true
        if (direction === SwipeDirection.none) //disable any swipe
            return false
        if (event.action == MotionEvent.ACTION_DOWN) {
            initialXValue = event.x
            return true
        }
        if (event.action == MotionEvent.ACTION_MOVE) {
            try {
                val diffX: Float = event.x - initialXValue
                if (diffX > 0 && direction === SwipeDirection.right) {
                    // swipe from left to right detected
                    return false
                } else if (diffX < 0 && direction === SwipeDirection.left) {
                    // swipe from right to left detected
                    return false
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
        return true
    }

    open fun setAllowedSwipeDirection(direction: SwipeDirection?) {
        this.direction = direction
    }
}