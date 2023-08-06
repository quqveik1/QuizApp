package com.kurlic.quizapp.gpt

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CallGPT {
    @Headers("Authorization: Bearer $OPENAIKEY")
    @POST("/v1/chat/completions")
    fun callGPT(@Body requestBody: ChatRequest): Call<ChatResponse>
}

data class ChatRequest(
    val messages: List<Message>,
    val model: String = "gpt-3.5-turbo",
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message

)