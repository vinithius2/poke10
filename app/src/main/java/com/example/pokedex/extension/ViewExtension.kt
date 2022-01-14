package com.example.pokedex.extension

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.Transformation
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.palette.graphics.Palette
import kotlin.math.max

fun View.setColorBackground(color: Palette.Swatch?) {
    val background = this.background
    if (background is GradientDrawable) {
        color?.let {
            background.setColor(it.rgb)
        }
    }
}

fun View.expand() {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)

    val targetHeight: Int = measuredHeight
    val duration = (targetHeight / context.resources.displayMetrics.density * 2).toLong()
    layoutParams.height = 1
    isVisible = true
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height = if (interpolatedTime == 1f)
                LinearLayout.LayoutParams.WRAP_CONTENT
            else
                max(1, (targetHeight * interpolatedTime).toInt())
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    animation.duration = duration
    startAnimation(animation)
}

fun View.collapse() {
    val initialHeight: Int = measuredHeight
    val duration = (initialHeight / context.resources.displayMetrics.density * 2).toLong()
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                isVisible = false
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    animation.duration = duration
    startAnimation(animation)
}

fun View.rotationFromBottomToTop() {
    val animation = RotateAnimation(
        -180f,
        0f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    animation.duration = 500
    startAnimation(animation)
    rotation = 0f
}

fun View.rotationFromTopToBottom() {
    val animation = RotateAnimation(
        180f,
        0f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    animation.duration = 500
    startAnimation(animation)
    rotation = 180f
}

fun View.getCollapseAndExpand(expand: Boolean, arrow: View): Boolean {
    if (expand) {
        arrow.rotationFromBottomToTop()
        this.collapse()
    } else {
        arrow.rotationFromTopToBottom()
        this.expand()
    }
    return !expand
}
