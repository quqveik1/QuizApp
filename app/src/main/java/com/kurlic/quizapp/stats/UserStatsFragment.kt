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

class UserStatsFragment : Fragment() {

    lateinit var userStats: UserStats

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_user_stats, container, false)

        userStats = loadUserStats()

        setUpCompletedQuestions(root)
        setUpCompletedRightQuestions(root)
        setUpProgressBar(root)
        setUpTotalTimeSpent(root)
        setUpPopularThemes(root)

        return root
    }

    private fun loadUserStats(): UserStats {
        val sharedPrefs = requireActivity().getSharedPreferences(UserStats.statsKey, Context.MODE_PRIVATE)
        val userStatsJson = sharedPrefs.getString(UserStats.statsKey, null)
        val gson = Gson()

        return if (userStatsJson != null) {
            gson.fromJson(userStatsJson, UserStats::class.java)
        } else {
            UserStats()
        }
    }

    private fun setUpCompletedQuestions(root: View) {
        val completedQuestions = root.findViewById<TextView>(R.id.totalNumberAnswersTextView)
        completedQuestions.text = userStats.completedQuestions.toString()
    }

    private fun setUpCompletedRightQuestions(root: View) {
        val completedRightQuestions = root.findViewById<TextView>(R.id.totalPercentageCorrectAnswersTextView)
        val percentage = calculatePercentage()
        completedRightQuestions.text = String.format("%.1f", percentage * 100) + "%"
        setTextViewPercentColor(percentage, completedRightQuestions, requireContext())
    }

    private fun calculatePercentage(): Float {
        return if (userStats.completedQuestions > 0) {
            (userStats.completedRightQuestions.toFloat() / userStats.completedQuestions)
        } else {
            0F
        }
    }

    private fun setUpProgressBar(root: View) {
        val bar = root.findViewById<ProgressBar>(R.id.percentageCorrectAnswersProgressBar)
        bar.max = userStats.completedQuestions
        bar.progress = userStats.completedRightQuestions
    }

    private fun setUpTotalTimeSpent(root: View) {
        val totalTimeSpent = root.findViewById<TextView>(R.id.totalTimeSpentTextView)
        setTimeToView(totalTimeSpent, userStats.totalTime)
    }

    private fun setUpPopularThemes(root: View) {
        val listThemes = userStats.themesQuestionsMap.toList().sortedByDescending { it.second }
        val recyclerView = root.findViewById<RecyclerView>(R.id.popularThemesRecyclerView)
        recyclerView.adapter = PopularThemesAdapter(listThemes)
    }


}