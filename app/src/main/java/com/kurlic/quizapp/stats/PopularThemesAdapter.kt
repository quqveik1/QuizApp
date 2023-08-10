package com.kurlic.quizapp.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.quizapp.R

class PopularThemesAdapter(private val themes: List<Pair<String, Int>>) :
    RecyclerView.Adapter<PopularThemesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.popular_theme_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (theme, count) = themes[position]
        holder.bind(theme, count)
    }

    override fun getItemCount(): Int = themes.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val popularThemeNameTextView: TextView = view.findViewById(R.id.popularThemeName)
        private val popularThemeNumberQuestionsTextView: TextView = view.findViewById(R.id.popularThemeNumberQuestions)

        fun bind(theme: String, count: Int) {
            popularThemeNameTextView.text = theme
            popularThemeNumberQuestionsTextView.text = count.toString()
        }
    }
}
