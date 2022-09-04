package com.example.restful_api_example.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout
import com.example.restful_api_example.ui.main.ShareViewModel
import timber.log.Timber
import kotlin.math.abs


class CustomLayout(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private lateinit var shareViewModel: ShareViewModel

    var isBeingDragged = false
    var isUnableToDrag = false
    var lastMotionX = 0f
    var lastMotionY = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isBeingDragged = false
                isUnableToDrag = false
            }
            MotionEvent.ACTION_DOWN -> {
                lastMotionX = ev.x
                lastMotionY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {

                val xDiff: Float = abs(ev.x - lastMotionX)
                val yDiff: Float = abs(ev.y - lastMotionY)

                val configuration = ViewConfiguration.get(context)
                val touchSlop = configuration.scaledPagingTouchSlop
                isBeingDragged = xDiff > yDiff && xDiff > touchSlop

                if (!isUnableToDrag && isBeingDragged) {
                    if (ev.x - lastMotionX > 0) {
                        Timber.d("Direction Right")
                        shareViewModel.navigationDirection.value = "Right"
                    } else {
                        Timber.d("Direction Left")
                        shareViewModel.navigationDirection.value = "Left"
                    }
                    isUnableToDrag = true
                }
            }
        }

        Timber.d( "onInterceptTouchEvent ${ev?.action} at " + lastMotionX + "," + lastMotionY
//                    + " mIsBeingDragged=" + isBeingDragged
//                    + "mIsUnableToDrag=" + isUnableToDrag
        )
        return isBeingDragged
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
//        Timber.d( "onTouchEvent ${ev?.action} at ${ev?.x} , ${ev?.y}")
        when (ev?.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isBeingDragged = false
                isUnableToDrag = false
            }
        }
        return super.onTouchEvent(ev)
    }

    fun setShareViewModel(shareViewModel: ShareViewModel) {
        this.shareViewModel = shareViewModel
    }
}