package com.kurlic.quizapp.game

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class GameData : Serializable, Parcelable
{
    var gameTheme = ""
    val questionsLen = 10
    var activeQuestion = 0

    var questions = mutableListOf<QuizQuestion>()

    fun getActiveQuestion() : QuizQuestion
    {
        return questions[activeQuestion]
    }
}