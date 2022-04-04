package com.example.mobilecw

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Scorecard: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scorecard)

        val correctScoreView = findViewById<TextView>(R.id.score_textview)
        val incorrectScoreView = findViewById<TextView>(R.id.incorrect_score)

        correctScoreView.append(correctAnswersCount.toString())
        incorrectScoreView.append(incorrectAnswersCount.toString())
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
