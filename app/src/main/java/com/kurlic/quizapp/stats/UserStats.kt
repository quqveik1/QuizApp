package com.kurlic.quizapp.stats

import android.os.Parcelable
import com.kurlic.quizapp.game.GameData
import java.io.Serializable

class UserStats : Serializable
{
    companion object
    {
        const val statsKey = "STATSKEY"
    }

    var totalTime: Long = 0

    var completedQuestions: Int = 0
    var completedRightQuestions: Int = 0

    var themesQuestionsMap: MutableMap<String, Int> = mutableMapOf()

    fun addNewGame(gameData: GameData)
    {
        totalTime               += gameData.finishTime - gameData.startTime

        completedQuestions      += gameData.questionsLen
        completedRightQuestions += gameData.rightAnswers

        val currentCount = themesQuestionsMap.getOrDefault(gameData.gameTheme, 0)
        themesQuestionsMap[gameData.gameTheme] = currentCount + gameData.questionsLen
    }
}