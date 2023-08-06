package com.kurlic.quizapp.home

import HomeButtonAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.quizapp.R
import com.kurlic.quizapp.gpt.ChatRequest
import com.kurlic.quizapp.gpt.ChatResponse
import com.kurlic.quizapp.gpt.GenerateQuizName
import com.kurlic.quizapp.gpt.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/") // URL должен заканчиваться на /
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(GenerateQuizName::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generateNames()

        viewAdapter = HomeButtonAdapter(emptyList())

        recyclerView = view.findViewById<RecyclerView>(R.id.buttonRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun generateNames() {
        val message = Message("system", "I need 10 interesting quiz topics. Ответь на русском. Ответ это просто темы без нумерации разделенные по строкам. Нельзя ставить цифру в начале строки")
        val request = ChatRequest(listOf(message))
        val call = api.generateNames(request)

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
        (recyclerView.adapter as HomeButtonAdapter).updateData(data)
    }
}