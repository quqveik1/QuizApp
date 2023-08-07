package com.kurlic.quizapp.server

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CallServer {

    @GET("/getdefault")
    fun getDefaultQuestions(@Query("theme") theme: String, @Query("len") len: Int) : Call<List<String>>
}
