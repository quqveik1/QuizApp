package com.kurlic.quizapp.common

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kurlic.quizapp.R
import java.util.concurrent.TimeUnit

fun setTextViewPercentColor(percents: Float, textView: TextView, context: Context)
{
    if(percents <= 0.33)
    {
        textView.setTextColor(ContextCompat.getColor(context, R.color.badResultColor))
    }
    else if(percents > 0.33 && percents < 0.66)
    {
        textView.setTextColor(ContextCompat.getColor(context, R.color.midResultColor))
    }
    else if(percents > 0.66)
    {
        textView.setTextColor(ContextCompat.getColor(context, R.color.goodResultColor))
    }
}

fun setTimeToView(textView: TextView, totalTime: Long)
{
    val minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime) % 60

    val timeStr = "$minutes:$seconds"

    textView.text = timeStr
}