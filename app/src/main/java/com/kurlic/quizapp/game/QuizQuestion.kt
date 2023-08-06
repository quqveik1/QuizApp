package com.kurlic.quizapp.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class QuizQuestion(
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
) : Parcelable, Serializable
