package com.kurlic.quizapp.home

import HomeButtonAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.quizapp.MainActivity
import com.kurlic.quizapp.R
import com.kurlic.quizapp.gpt.ChatRequest
import com.kurlic.quizapp.gpt.ChatResponse
import com.kurlic.quizapp.gpt.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        //generateNames()
        loadTopics()


        recyclerView = view.findViewById<RecyclerView>(R.id.buttonRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun loadTopics()
    {
        val quizTopics = resources.getStringArray(R.array.quiz_topics).toList()
        viewAdapter = HomeButtonAdapter(quizTopics)
    }

    private fun generateNames() {
        val message = Message("system", "I need 10 interesting quiz topics. Ответь на русском. Ответ это просто темы без нумерации разделенные по строкам. Нельзя ставить цифру в начале строки")
        val request = ChatRequest(listOf(message))
        val call = MainActivity.createApi().callGPT(request)

        call.enqueue(object : Callback<ChatResponse> {
            override fun onResponse(
                call: Call<ChatResponse>,
                response: Response<ChatResponse>
            ) {
                updateRecyclerView(response.body()!!.choices[0].message.content)
            }

            override fun onFailure(
                call: Call<ChatResponse>,
                t: Throwable
            ) {
                Toast.makeText(context, "???", Toast.LENGTH_SHORT).show()
            }
        })
        }

    private fun updateRecyclerView(topics: String?) {
        val data = topics?.split("\n") ?: listOf()
        // передать их в адаптер
        (viewAdapter as HomeButtonAdapter).updateData(data)
    }
}