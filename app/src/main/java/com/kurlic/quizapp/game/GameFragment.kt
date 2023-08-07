package com.kurlic.quizapp.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.kurlic.quizapp.MainActivity
import com.kurlic.quizapp.R
import com.kurlic.quizapp.gpt.ChatRequest
import com.kurlic.quizapp.gpt.ChatResponse
import com.kurlic.quizapp.gpt.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameFragment : Fragment() {

    companion object {
        val NAMEKEY =
            "NameKey"
    }

    var gameName = ""

    var questionsProgressBar: ProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val rootview = inflater.inflate(R.layout.game_fragment, container, false)

        questionsProgressBar = rootview.findViewById(R.id.questionsProgressBar)

        loadName(rootview, savedInstanceState)

        getQuestions()

        return rootview
    }

    private fun loadName(rootView: View, savedInstanceState: Bundle?)
    {
        if(savedInstanceState != null)
        {
            gameName = savedInstanceState.getString(NAMEKEY, "");
        }
        if(arguments != null)
        {
            gameName = requireArguments().getString(NAMEKEY, "");
        }

        val tv = rootView.findViewById<TextView>(R.id.quizName)

        tv.text = gameName
    }

    private val questionsLen = 2
    var activeQuestion = 0
        set(value) {
            field = value

            if(questionsProgressBar != null)
            {
                questionsProgressBar!!.progress = ((activeQuestion.toFloat() / questionsLen.toFloat() ) * 100).toInt()
            }


        }
    private val questions = mutableListOf<QuizQuestion>()

    private fun getQuestions()
    {
        getServerQuestions()
    }

    private fun getServerQuestions()
    {
        val call = MainActivity.createServerApi().getDefaultQuestions(gameName, questionsLen)

        call.enqueue(object : Callback<List<String>>{
            override fun onResponse(
                call: Call<List<String>>,
                response: Response<List<String>>
            )
            {
                if(!response.isSuccessful)
                {
                    getServerQuestions()
                    Toast.makeText(context, "???" + response.message(), Toast.LENGTH_SHORT).show()
                    Log.e("???", response.message()!!);
                    return
                }

                val list = response.body()

                if(list == null)
                {
                    getServerQuestions()
                    return
                }

                val gson = Gson()

                for(s in list)
                {
                    val obj = gson.fromJson(s, QuizQuestion::class.java)

                    questions.add(obj)
                }

                startQuiz()
            }

            override fun onFailure(
                call: Call<List<String>>,
                t: Throwable
            ) {
                Toast.makeText(context, "???" + t.message, Toast.LENGTH_SHORT).show()
                Log.e("???", t.message!!);
            }
        })
    }

    private fun getAiQuestions()
    {
        val message = Message("system", "Сгенирируй $questionsLen интересных вопросов по теме \"$gameName\". Ответ должен быть возвращен в формате валидного json-массива  размером $questionsLen. Каждый элемент которого выглядит так: {\n" +
                "    \"question\": \"Содержание вопроса\",\n" +
                "    \"answers\": [\"Ответ 0\", \"Ответ 1\", \"Ответ 2\", \"Ответ 3\"],\n" +
                "    \"correctAnswerIndex\": номер правильного ответа от 0 до 3 включительно\n" +
                "  }")
        val request = ChatRequest(listOf(message))
        questionsCall = MainActivity.createApi().callGPT(request)

        questionsCall?.enqueue(object : Callback<ChatResponse> {
            override fun onResponse(
                call: Call<ChatResponse>,
                response: Response<ChatResponse>
            ) {

                Toast.makeText(context, "answer", Toast.LENGTH_SHORT).show()

                deserialize(response.body()!!.choices[0].message.content)
                startQuiz()
            }

            override fun onFailure(
                call: Call<ChatResponse>,
                t: Throwable
            ) {
                Toast.makeText(context, "???", Toast.LENGTH_SHORT).show()
                getQuestions()
            }
        })
    }


    var questionsCall: Call<ChatResponse>? = null

    override fun onDestroy() {
        super.onDestroy()
        // Отменить запрос, если он все еще выполняется.
        if (questionsCall?.isExecuted == false) {
            questionsCall?.cancel()
        }
    }

    private fun startQuiz() {
        activeQuestion = 0
        showActiveQuestion()
    }

    private fun showActiveQuestion() {
        val question = questions[activeQuestion]

        // Создайте новый экземпляр фрагмента вопроса и передайте ему данные вопроса.
        val questionFragment = QuestionFragment().apply {
            arguments = Bundle().apply {
                putParcelable(QuestionFragment.QUESTION_KEY, question)
            }
        }

        // Установите слушатель ответов для этого вопроса.
        questionFragment.questionAnswerCallBack = (object : QuestionAnswerCallBack {
            override fun onQuestionAnswered(isRight: Boolean) {
                activeQuestion++
                if (activeQuestion < questions.size) {
                    // Если есть следующий вопрос, покажите его.
                    showActiveQuestion()
                } else {
                    // Если больше нет вопросов, завершите игру.
                    endQuiz()
                }
            }
        })

        // Замените контейнер вопроса на новый фрагмент вопроса.
        childFragmentManager.beginTransaction()
            .replace(R.id.questionsContainer, questionFragment)
            .commit()
    }

    private fun endQuiz() {
        findNavController().popBackStack()
    }

    private fun deserialize(string: String)
    {
        val gson = Gson()
        val quizQuestionsArray = gson.fromJson(string, Array<QuizQuestion>::class.java)

        questions.addAll(quizQuestionsArray)
    }

}