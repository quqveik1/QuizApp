package com.kurlic.quizapp.game

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.kurlic.quizapp.MainActivity
import com.kurlic.quizapp.R
import com.kurlic.quizapp.common.loadImage
import com.kurlic.quizapp.gpt.ChatRequest
import com.kurlic.quizapp.gpt.ChatResponse
import com.kurlic.quizapp.gpt.Message
import com.kurlic.quizapp.server.CallServer
import com.kurlic.quizapp.stats.UserStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    lateinit var longWaitTextView: TextView

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
        longWaitTextView = rootview.findViewById(R.id.longLoadTextView)

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
    private var shouldReconnect = true
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
                    reconnect(response.message())
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

                if(activity != null)
                {
                    requireActivity().runOnUiThread {
                        createAndStartQuiz()
                    }
                }
            }

            override fun onFailure(
                call: Call<List<String>>,
                t: Throwable)
            {
                reconnect(t.message!!)
            }
        })
    }

    private fun reconnect(error: String)
    {
        if(!shouldReconnect) return
        Log.e("Questions", error)

        if(longWaitTextView.visibility != View.VISIBLE)
        {
            if(activity != null)
            {
                requireActivity().runOnUiThread {
                    longWaitTextView.visibility = View.VISIBLE
                }
            }
        }

        getServerQuestions()
    }

    var questionsCall: Call<ChatResponse>? = null

    override fun onDestroy() {
        super.onDestroy()
        questionsCall?.cancel()
        serverCall?.cancel()
        shouldReconnect = false
    }

    private fun startQuiz() {
        dataLoadProgressBar?.visibility = View.INVISIBLE
        longWaitTextView.visibility = View.INVISIBLE
        showActiveQuestion()
    }
    private fun createQuiz() {
        gameData.startTime = System.currentTimeMillis()
    }
    private fun createAndStartQuiz() {
        createQuiz()
        startQuiz()
    }

    private fun showActiveQuestion() {
        val question = gameData.getActiveQuestion()
        setProgressBar()

        val questionFragment = QuestionFragment().apply {
            arguments = Bundle().apply {
                putParcelable(QuestionFragment.QUESTION_KEY, question)
            }
        }

        questionFragment.questionAnswerCallBack = (object : QuestionAnswerCallBack
        {
            override fun onQuestionAnswered(isRight: Boolean)
            {
                if(isRight)
                {
                    gameData.rightAnswers++;
                }

                gameData.activeQuestion++
                if (gameData.activeQuestion < gameData.questions.size)
                {
                    showActiveQuestion()
                }
                else
                {
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

        gameData.finishTime = System.currentTimeMillis()

        val bundle = Bundle()

        bundle.putParcelable(GameStatsFragment.gameDataKey, gameData)

        lifecycleScope.launch(Dispatchers.IO)
        {
            val sharedPrefs = requireActivity().getSharedPreferences(UserStats.statsKey, Context.MODE_PRIVATE)
            val gson = Gson()

            val userStatsJson = sharedPrefs.getString(UserStats.statsKey, null)
            val userStats = if (userStatsJson != null)
            {
                gson.fromJson(userStatsJson, UserStats::class.java)
            } else
            {
                UserStats()
            }

            userStats.addNewGame(gameData)

            val editor = sharedPrefs.edit()
            editor.putString(UserStats.statsKey, gson.toJson(userStats))
            editor.apply()
        }


        requireActivity().runOnUiThread {
            findNavController().navigate(R.id.action_GameFragment_to_GameStatsFragment, bundle)
        }
    }

    private fun deserialize(string: String)
    {
        val gson = Gson()
        val quizQuestionsArray = gson.fromJson(string, Array<QuizQuestion>::class.java)

        gameData.questions.addAll(quizQuestionsArray)
    }

}