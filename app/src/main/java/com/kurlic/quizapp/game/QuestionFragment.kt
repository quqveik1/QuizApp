package com.kurlic.quizapp.game

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kurlic.quizapp.R

class QuestionFragment : Fragment() {

    private lateinit var question: QuizQuestion

    companion object
    {
        val QUESTION_KEY = "QUESTION_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null)
        {
            question =
                requireArguments().getParcelable(QUESTION_KEY)!!
        }
    }

    var buttonList = mutableListOf<Button>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview = inflater.inflate(R.layout.question_fragment, container, false)

        val questionTextView: TextView = rootview.findViewById(R.id.questionTextView)
        val button0: Button = rootview.findViewById(R.id.button0)
        val button1: Button = rootview.findViewById(R.id.button1)
        val button2: Button = rootview.findViewById(R.id.button2)
        val button3: Button = rootview.findViewById(R.id.button3)

        buttonList.add(button0)
        buttonList.add(button1)
        buttonList.add(button2)
        buttonList.add(button3)

        questionTextView.text = question.question

        setButtons()

        return rootview
    }

    private fun setButtons()
    {
        for (i in 0 until buttonList.size)
        {
            buttonList[i].text = question.answers[i]
            buttonList[i].setOnClickListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    questionAnswerCallBack?.onQuestionAnswered(question.correctAnswerIndex == i)
                }, 2000)
                highlightButtons()}
        }
    }

    private fun highlightButtons()
    {
        for (i in 0 until buttonList.size)
        {
            val color = if(question.correctAnswerIndex == i) requireContext().getColor(R.color.trueColor) else requireContext().getColor(R.color.falseColor)
            buttonList[i].setBackgroundColor(color)
        }
    }

    var questionAnswerCallBack: QuestionAnswerCallBack? = null
}