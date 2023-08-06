package com.kurlic.quizapp.home

import HomeButtonAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.quizapp.R

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = List(10) { "Recylerview $it" }

        viewAdapter = HomeButtonAdapter(data)

        recyclerView = view.findViewById<RecyclerView>(R.id.buttonRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }
}