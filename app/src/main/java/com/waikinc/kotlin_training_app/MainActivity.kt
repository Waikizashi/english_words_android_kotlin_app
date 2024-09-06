package com.waikinc.kotlin_training_app

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.waikinc.kotlin_training_app.databinding.ActivityLearnWordBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityLearnWordBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for ActivityLearnWordBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLearnWordBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.navigationBars() or WindowInsets.Type.statusBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }

        val trainer = LearnWordsTrainer()
        showNextQuestion(trainer)

        with(binding){
            progressBar.progress = 0
            progressBar.max = trainer.getQuestionsCount()
            btnContinue.setOnClickListener {
                layoutResult.isVisible = false
                markAnswerNeutral(layoutAnswer1, tvVariantNumber1, tvVariantValue1)
                markAnswerNeutral(layoutAnswer2, tvVariantNumber2, tvVariantValue2)
                markAnswerNeutral(layoutAnswer3, tvVariantNumber3, tvVariantValue3)
                markAnswerNeutral(layoutAnswer4, tvVariantNumber4, tvVariantValue4)
                showNextQuestion(trainer)
            }

            btnSkip.setOnClickListener {
                showNextQuestion(trainer)
                markAnswerNeutral(layoutAnswer1, tvVariantNumber1, tvVariantValue1)
                markAnswerNeutral(layoutAnswer2, tvVariantNumber2, tvVariantValue2)
                markAnswerNeutral(layoutAnswer3, tvVariantNumber3, tvVariantValue3)
                markAnswerNeutral(layoutAnswer4, tvVariantNumber4, tvVariantValue4)
            }
            ibClose.setOnClickListener {
                finishAffinity()
            }
        }

    }

    private fun showNextQuestion(trainer: LearnWordsTrainer) {
        val firstQuestion: Question? = trainer.getNextQuestion()
        with(binding) {
            if (firstQuestion == null || firstQuestion.variants.size < NUMBER_OF_ANSWERS) {
                tvQuestionWord.isVisible = false
                layoutVariants.isVisible = false
                btnSkip.isVisible = false
                layoutFinalMsg.isVisible = true
            } else{
                btnSkip.isVisible = true
                tvQuestionWord.isVisible = true
                tvQuestionWord.text = firstQuestion.correctAnswer.original

                tvVariantValue1.text = firstQuestion.variants[0].translate
                tvVariantValue2.text = firstQuestion.variants[1].translate
                tvVariantValue3.text = firstQuestion.variants[2].translate
                tvVariantValue4.text = firstQuestion.variants[3].translate

                layoutAnswer1.setOnClickListener {
                    if (trainer.checkAnswer(0)){
                        markAnswerCorrect(layoutAnswer1, tvVariantNumber1, tvVariantValue1)
                        showResultMessage(true)
                    } else{
                        markAnswerWrong(layoutAnswer1, tvVariantNumber1, tvVariantValue1)
                        showResultMessage(false)
                    }
                }
                layoutAnswer2.setOnClickListener {
                    if (trainer.checkAnswer(1)){
                        markAnswerCorrect(layoutAnswer2, tvVariantNumber2, tvVariantValue2)
                        showResultMessage(true)
                    } else{
                        markAnswerWrong(layoutAnswer2, tvVariantNumber2, tvVariantValue2)
                        showResultMessage(false)
                    }
                }
                layoutAnswer3.setOnClickListener {
                    if (trainer.checkAnswer(2)){
                        markAnswerCorrect(layoutAnswer3, tvVariantNumber3, tvVariantValue3)
                        showResultMessage(true)
                    } else{
                        markAnswerWrong(layoutAnswer3, tvVariantNumber3, tvVariantValue3)
                        showResultMessage(false)
                    }
                }
                layoutAnswer4.setOnClickListener {
                    if (trainer.checkAnswer(3)){
                        markAnswerCorrect(layoutAnswer4, tvVariantNumber4, tvVariantValue4)
                        showResultMessage(true)
                    } else{
                        markAnswerWrong(layoutAnswer4, tvVariantNumber4, tvVariantValue4)
                        showResultMessage(false)
                    }
                }
            }
        }
    }

    private fun markAnswerNeutral(
        layoutAnswer: LinearLayout,
        tvVariantNumber: TextView,
        tvVariantNumberValue: TextView,
    ) {
        layoutAnswer.background = ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.shape_rounded_containers
        )
        tvVariantNumberValue.setTextColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.textVariantsColor
            )
        )
        tvVariantNumber.apply {
            background = ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.shape_rounded_variants
            )
            setTextColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.textVariantsColor
                )
            )
        }
    }

    private fun markAnswerWrong(
        layoutAnswer: LinearLayout,
        tvVariantNumber: TextView,
        tvVariantNumberValue: TextView,
    ) {
        layoutAnswer.background = ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.shape_rounded_containers_wrong
        )
        tvVariantNumber.background = ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.shape_rounded_variants_wrong
        )
        tvVariantNumber.setTextColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.white
            )
        )
        tvVariantNumberValue.setTextColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.wrongAnswerColor
            )
        )
    }

    private fun markAnswerCorrect(
        layoutAnswer: LinearLayout,
        tvVariantNumber: TextView,
        tvVariantValue: TextView,
    ) {

        binding.progressBar.progress ++
        layoutAnswer.background = ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.shape_rounded_containers_correct
        )
        tvVariantNumber.background = ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.shape_rounded_variants_correct
        )
        tvVariantNumber.setTextColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.white
            )
        )
        tvVariantValue.setTextColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.correctAnswerColor
            )
        )

    }

    private fun showResultMessage(isCorrect: Boolean) {
        val color: Int
        val messageText: String
        val resultIconResource: Int

        if(isCorrect){
            color = ContextCompat.getColor(this, R.color.correctAnswerColor)
            messageText = resources.getString(R.string.title_correct)
            resultIconResource = R.drawable.ic_correct
        } else {
            color = ContextCompat.getColor(this, R.color.wrongAnswerColor)
            messageText = resources.getString(R.string.title_wrong)
            resultIconResource = R.drawable.ic_wrong
        }


        with(binding){
            btnSkip.isVisible = false
            layoutResult.isVisible = true
            btnContinue.setTextColor(color)
            layoutResult.setBackgroundColor(color)
            tvResultMessage.text = messageText
            ivResultIcon.setImageResource(resultIconResource)
        }
    }
}