package com.sergiofraile.timefighter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var tapMeButton: Button
    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView

    private var score = 0
    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown: Long = 60000
    private val countDownInterval: Long = 1000
    private val TAG = MainActivity::class.java.simpleName
    private var timeLeftOnTimer: Long = 60000

    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private  val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"onCreate called. Score is: $score")

        tapMeButton = findViewById(R.id.tap_me_button)
        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)

        tapMeButton.setOnClickListener { _ ->
            incrementScore()
        }

        resetGame()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt(SCORE_KEY, score)
        outState?.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState: Saving score: $score and Time Left: $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy called.")
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }
        score++;
        updateScoreTextView()
    }

    private fun updateScoreTextView() {
        gameScoreTextView.text = getString(R.string.your_score,  score.toString())
    }

    private fun updateTimeLeftTextView(timeLeft: Long) {
        timeLeftTextView.text = getString(R.string.time_left,  timeLeft.toString())
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this,   getString(R.string.game_over_message, score.toString()), Toast.LENGTH_LONG).show()
        resetGame()
    }

    private fun resetGame() {
        score = 0
        updateScoreTextView()

        val initialTimeLeft = initialCountDown / countDownInterval
        updateTimeLeftTextView(initialTimeLeft)

        countDownTimer = object: CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(p0: Long) {
                timeLeftOnTimer = p0
                updateTimeLeftTextView(p0/countDownInterval)
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }
}
