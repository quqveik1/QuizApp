package com.kurlic.quizapp.common

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kurlic.quizapp.server.ThemesPicsNumMap
import com.kurlic.quizapp.server.categoryTranslations
import java.util.Locale

fun getImageResource(category: String, context: Context, needStaticElement: Boolean = false): Int {

    val translatedText = categoryTranslations[category] ?: category
    var resourceName = translatedText.lowercase(Locale.ENGLISH)

    var iconMaxIndex = 1

    val hasMultiPics: Boolean
    if (needStaticElement) {
        hasMultiPics = false
    } else {
        hasMultiPics = ThemesPicsNumMap.contains(resourceName)
        if (hasMultiPics) {
            iconMaxIndex = ThemesPicsNumMap[resourceName]!!
        }
    }

    resourceName += "_icon"

    if (hasMultiPics) {
        val num = (0 until iconMaxIndex).random()

        resourceName += "_" + num.toString()
    } else {
        resourceName += "_0"
    }

    return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
}

fun loadImage(category: String, context: Context, imageView: ImageView, needStaticElement: Boolean = false) {
    val imageResource = getImageResource(category, context, needStaticElement)
    Glide.with(context).load(imageResource).into(imageView)

}