package com.example.pokedex.extension

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import android.widget.ImageView

fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar(Char::uppercase)
}

fun String.getIdIntoUrl(): String? {
    try {
        val parse = Uri.parse(this)
        parse.pathSegments.getOrNull(3)?.let {
            return it
        }
    } catch (e: Exception) {
        return null
    }
    return null
}

fun String.setDrawableIco(context: Context, image: ImageView) {
    try {
        val resource = context.resources.getIdentifier(
            this, "drawable", context.packageName
        )
        val drawable = context.resources.getDrawable(resource, null)
        image.setImageDrawable(drawable)
    } catch (e: Resources.NotFoundException) {
        Log.e("ERROR Ico type", "No type image")
    }
}
