package com.kurlic.quizapp.stats

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kurlic.quizapp.R
import com.kurlic.quizapp.common.setTextViewPercentColor
import com.kurlic.quizapp.common.setTimeToView

class UserStatsFragment : Fragment()
{

    lateinit var userStats: UserStats

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View?
    {
        val root = inflater.inflate(R.layout.fragment_user_stats, container, false)

        val sharedPrefs = requireActivity().getSharedPreferences(UserStats.statsKey, Context.MODE_PRIVATE)
        val userStatsJson = sharedPrefs.getString(UserStats.statsKey, null)
        val gson = Gson()

        userStats = if (userStatsJson != null)
        {
            gson.fromJson(userStatsJson, UserStats::class.java)
        }
        else
        {
            UserStats()
        }

        val completedQuestions = root.findViewById<TextView>(R.id.totalNumberAnswersTextView)
        completedQuestions.text = userStats.completedQuestions.toString()

        val completedRightQuestions = root.findViewById<TextView>(R.id.totalPercentageCorrectAnswersTextView)
        val percentage = ((userStats.completedRightQuestions.toFloat() / userStats.completedQuestions))
        completedRightQuestions.text = String.format("%.1f", percentage * 100) + "%"
        setTextViewPercentColor(percentage, completedRightQuestions, requireContext())

        val bar = root.findViewById<ProgressBar>(R.id.percentageCorrectAnswersProgressBar)
        bar.max = userStats.completedQuestions
        bar.progress = userStats.completedRightQuestions

        val totalTimeSpent = root.findViewById<TextView>(R.id.totalTimeSpentTextView)
        setTimeToView(totalTimeSpent, userStats.totalTime)

        val listThemes = userStats.themesQuestionsMap.toList().sortedByDescending { it.second }
        val recyclerView = root.findViewById<RecyclerView>(R.id.popularThemesRecyclerView)
        recyclerView.adapter = PopularThemesAdapter(listThemes)

        return root
    }

}