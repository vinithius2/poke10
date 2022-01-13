package com.example.pokedex.extension

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.palette.graphics.Palette

fun View.setColorBackground(color: Palette.Swatch?) {
    val background = this.background
    if (background is GradientDrawable) {
        color?.let {
            background.setColor(it.rgb)
        }
    }
}