package com.kurlic.quizapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playButton = view.findViewById<Button>(R.id.playButton)
        playButton.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_HomeFragment)
        }

        val statsButton = view.findViewById<Button>(R.id.userStatisticsButton)
        statsButton.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_UserStatsFragment)
        }
    }
}