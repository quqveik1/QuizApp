package com.kurlic.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kurlic.quizapp.gpt.CallGPT
import com.kurlic.quizapp.server.CallServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)
    }


    companion object {
        fun createApi() : CallGPT
        {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(CallGPT::class.java)
        }
        fun createServerApi() : CallServer
        {
            val retrofit = Retrofit.Builder()
                //.baseUrl("http://192.168.10.102:8080/")
                .baseUrl("https://quiz-server-lbs9.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(CallServer::class.java)
        }
    }
}