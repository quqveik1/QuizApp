package com.kurlic.quizapp.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.kurlic.quizapp.R
import java.util.concurrent.TimeUnit

class GameStatsFragment : Fragment()
{
    companion object
    {
        val gameDataKey = "GDK"
    }

    private lateinit var gameData: GameData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_game_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        loadGameData(savedInstanceState)

        setCorrectAnswers(view)
        setTakenTime(view)

        val returnHomeButtons: Button = view.findViewById(R.id.returnHomeButton)

        returnHomeButtons.setOnClickListener {
            findNavController().navigate(R.id.action_GameStatsFragment_to_HomeFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        saveGameData(outState)
        super.onSaveInstanceState(outState)
    }

    private fun setCorrectAnswers(view: View)
    {
        val correctAnswersTextView: TextView = view.findViewById(R.id.numberCorrectAnswersTextView)
        val correctAnswersProgressBar: ProgressBar = view.findViewById(R.id.correctAnswersProgressBar)

        correctAnswersTextView.text = gameData.rightAnswers.toString() + "/" + gameData.questionsLen.toString()
        val percents = gameData.rightAnswers.toFloat() / gameData.questionsLen.toFloat()

        if(percents <= 0.33)
        {
            correctAnswersTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.badResultColor))
        }
        else if(percents > 0.33 && percents < 0.66)
        {
            correctAnswersTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.midResultColor))
        }
        else if(percents > 0.66)
        {
            correctAnswersTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.goodResultColor))
        }

        correctAnswersProgressBar.max = gameData.questionsLen
        correctAnswersProgressBar.progress = gameData.rightAnswers
    }

    private fun setTakenTime(view: View)
    {
        val timeTakenTextView: TextView = view.findViewById(R.id.timeTakenTextView)

        val totalTime = gameData.finishTime - gameData.startTime

        val minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime) % 60

        val timeStr = "$minutes:$seconds"

        timeTakenTextView.text = timeStr
    }

    private fun loadGameData(savedInstanceState: Bundle?)
    {
        if(savedInstanceState != null)
        {
            gameData = savedInstanceState.getParcelable(gameDataKey)!!
        }
        else
        {
            if(arguments != null)
            {
                gameData = requireArguments().getParcelable(gameDataKey) ?: GameData()
            }
        }
    }

    private fun saveGameData(bundle: Bundle)
    {
        bundle.putParcelable(gameDataKey, gameData)
    }
}