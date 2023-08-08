package com.kurlic.quizapp.common

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kurlic.quizapp.server.categoryTranslations
import java.util.Locale

fun getImageResource(category: String, context: Context): Int {

    val translatedText = categoryTranslations[category] ?: category
    var resourceName = translatedText.lowercase(Locale.ENGLISH)

    resourceName += "_icon"
    return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
}

fun loadImage(category: String, context: Context, imageView: ImageView)
{
    val imageResource = getImageResource(category, context)
    Glide.with(context)
        .load(imageResource)
        .into(imageView)

}