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
import com.kurlic.quizapp.common.loadImage
import com.kurlic.quizapp.gpt.ChatRequest
import com.kurlic.quizapp.gpt.ChatResponse
import com.kurlic.quizapp.gpt.Message
import com.kurlic.quizapp.server.CallServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameFragment : Fragment() {

    companion object {
        val NAMEKEY =
            "NameKey"
    }

    val gameDataKey = "GDK"
    lateinit var gameData: GameData

    var questionsProgressBar: ProgressBar? = null
    var dataLoadProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        loadGameData(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val rootview = inflater.inflate(R.layout.game_fragment, container, false)

        questionsProgressBar = rootview.findViewById(R.id.questionsProgressBar)
        dataLoadProgressBar = rootview.findViewById(R.id.dataLoadProgressBar)

        loadName(rootview)

        loadQuestions()

        return rootview
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        saveGameData(outState)
        super.onSaveInstanceState(outState)
    }

    private fun loadName(rootView: View)
    {
        val tv = rootView.findViewById<TextView>(R.id.quizName)

        tv.text = gameData.gameTheme
    }

    private fun saveGameData(outState: Bundle)
    {
        outState.putParcelable(gameDataKey, gameData)
    }

    private fun loadGameData(savedInstanceState: Bundle?)
    {
        if(savedInstanceState != null)
        {
            gameData = savedInstanceState.getParcelable(gameDataKey)!!
        }
        else
        {
            gameData = GameData()
            if(arguments != null)
            {
                gameData.gameTheme = requireArguments().getString(NAMEKEY, "");
            }
        }
    }

    private fun loadQuestions()
    {
        if(gameData.questions.size == gameData.questionsLen)
        {
            startQuiz()
        }
        else
        {
            startLoad()
            getQuestions()
        }
    }

    private fun startLoad()
    {
        dataLoadProgressBar?.visibility = View.VISIBLE
    }

    private fun getQuestions()
    {
        getServerQuestions()
    }

    var serverCall: Call<List<String>>? = null
    private fun getServerQuestions()
    {
        serverCall = MainActivity.createServerApi().getDefaultQuestions(gameData.gameTheme, gameData.questionsLen)

        serverCall!!.enqueue(object : Callback<List<String>>{
            override fun onResponse(
                call: Call<List<String>>,
                response: Response<List<String>>
            )
            {
                if(!response.isSuccessful)
                {
                    getServerQuestions()
                    Log.e("Questions", response.message());
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

                    gameData.questions.add(obj)
                }

                startQuiz()
            }

            override fun onFailure(
                call: Call<List<String>>,
                t: Throwable
            ) {
                Log.e("Questions", t.message!!);
                getServerQuestions()
            }
        })
    }

    private fun getAiQuestions()
    {
        val message = Message("system", "Сгенирируй ${gameData.questionsLen} интересных вопросов по теме \"${gameData.gameTheme}\". Ответ должен быть возвращен в формате валидного json-массива  размером ${gameData.questionsLen}. Каждый элемент которого выглядит так: {\n" +
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
        if (serverCall?.isExecuted == false) {
            serverCall?.cancel()
        }
    }

    private fun startQuiz() {
        dataLoadProgressBar?.visibility = View.INVISIBLE
        showActiveQuestion()
    }

    private fun showActiveQuestion() {
        val question = gameData.getActiveQuestion()
        setProgressBar()

        // Создайте новый экземпляр фрагмента вопроса и передайте ему данные вопроса.
        val questionFragment = QuestionFragment().apply {
            arguments = Bundle().apply {
                putParcelable(QuestionFragment.QUESTION_KEY, question)
            }
        }

        // Установите слушатель ответов для этого вопроса.
        questionFragment.questionAnswerCallBack = (object : QuestionAnswerCallBack {
            override fun onQuestionAnswered(isRight: Boolean) {
                gameData.activeQuestion++
                if (gameData.activeQuestion < gameData.questions.size) {
                    // Если есть следующий вопрос, покажите его.
                    showActiveQuestion()
                } else {
                    // Если больше нет вопросов, завершите игру.
                    endQuiz()
                }
            }
        })

        childFragmentManager.beginTransaction()
            .replace(R.id.questionsContainer, questionFragment)
            .commit()
    }
    private fun setProgressBar()
    {
        if(questionsProgressBar != null)
        {
            questionsProgressBar!!.progress = ((gameData.activeQuestion.toFloat() / gameData.questionsLen.toFloat() ) * 100).toInt()
        }
    }

    private fun endQuiz() {
        findNavController().popBackStack()
    }

    private fun deserialize(string: String)
    {
        val gson = Gson()
        val quizQuestionsArray = gson.fromJson(string, Array<QuizQuestion>::class.java)

        gameData.questions.addAll(quizQuestionsArray)
    }

}