package com.kurlic.quizapp.common

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kurlic.quizapp.server.ThemeListOf5Pics
import com.kurlic.quizapp.server.categoryTranslations
import java.util.Locale
import kotlin.random.Random

fun getImageResource(category: String, context: Context, needStaticElement: Boolean = false): Int {

    val translatedText = categoryTranslations[category] ?: category
    var resourceName = translatedText.lowercase(Locale.ENGLISH)

    val hasMultiPics: Boolean = if(needStaticElement)
    {
        false
    }
    else
    {
        ThemeListOf5Pics.contains(resourceName)
    }

    resourceName += "_icon"

    if(hasMultiPics)
    {
        val num = (0..4).random()

        resourceName += "_" + num.toString()
    }
    else
    {
        resourceName += "_0"
    }

    return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
}

fun loadImage(category: String, context: Context, imageView: ImageView, needStaticElement: Boolean = false)
{
    val imageResource = getImageResource(category, context, needStaticElement)
    Glide.with(context)
        .load(imageResource)
        .into(imageView)

}