package com.kurlic.quizapp.game

import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kurlic.quizapp.R
import com.kurlic.quizapp.common.loadImage

@Suppress("DEPRECATION")
class QuestionFragment : Fragment() {

    private lateinit var question: QuizQuestion
    private lateinit var questionImage: ImageView

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

        questionImage = rootview.findViewById(R.id.questionImage)

        loadImage((parentFragment as GameFragment).gameData.gameTheme, requireContext(), questionImage)

        return rootview
    }

    private val answerDelay = 500L

    private fun setButtons()
    {
        for (i in 0 until buttonList.size)
        {
            buttonList[i].text = question.answers[i]
            buttonList[i].setOnClickListener {
                highlightButtons()
                Handler(Looper.getMainLooper()).postDelayed({
                    questionAnswerCallBack?.onQuestionAnswered(question.correctAnswerIndex == i)
                }, answerDelay)}
        }
    }

    private fun highlightButtons()
    {
        for (i in 0 until buttonList.size)
        {
            val colorResource = if(question.correctAnswerIndex == i) R.color.trueColor else R.color.falseColor
            val color = ContextCompat.getColor(requireContext(), colorResource)

            val colorStateList = ColorStateList.valueOf(color)
            buttonList[i].backgroundTintList = colorStateList


        }
    }

    var questionAnswerCallBack: QuestionAnswerCallBack? = null
}