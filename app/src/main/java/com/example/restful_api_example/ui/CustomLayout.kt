package com.example.restful_api_example.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import com.example.restful_api_example.R
import com.example.restful_api_example.ui.main.ShareViewModel
import com.example.restful_api_example.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.view.*
import timber.log.Timber
import kotlin.math.abs


class CustomLayout: LinearLayout {
    private lateinit var shareViewModel: ShareViewModel

    constructor(context: Context): super(context) {
        init(context, null)
    }
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
    }

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