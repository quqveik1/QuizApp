package com.kurlic.quizapp.common

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kurlic.quizapp.R
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun setTextViewPercentColor(percents: Float, textView: TextView, context: Context) {
    if (percents <= 0.33) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.badResultColor))
    } else if (percents > 0.33 && percents < 0.66) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.midResultColor))
    } else if (percents > 0.66) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.goodResultColor))
    }
}

fun setTimeToView(textView: TextView, totalTime: Long) {
    val format = SimpleDateFormat("mm:ss", Locale.getDefault())
    val timeStr = format.format(Date(totalTime))

    textView.text = timeStr
}