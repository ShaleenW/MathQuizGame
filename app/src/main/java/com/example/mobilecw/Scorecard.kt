package com.example.mobilecw

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Scorecard: AppCompatActivity() {

//    private var correctAnswersCount= 10
//    private var incorrectAnswersCount= 5

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val correctScoreView = findViewById<TextView>(R.id.score_textview)
        val incorrectScoreView = findViewById<TextView>(R.id.incorrect_score)
        val correctScoreText = findViewById<TextView>(R.id.textView2)
        val incorrectScoreText = findViewById<TextView>(R.id.textView3)

        val restoreData1 : CharSequence = correctScoreView.text
        val restoreData2 : CharSequence = incorrectScoreView.text
        val restoreData3 : CharSequence = correctScoreText.text
        val restoreData4 : CharSequence = incorrectScoreText.text

        outState.putCharSequence ("Data restored1", restoreData1)
        outState.putCharSequence ("Data restored2", restoreData2)
        outState.putCharSequence ("Data restored3", restoreData3)
        outState.putCharSequence ("Data restored4", restoreData4)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val correctScoreView = findViewById<TextView>(R.id.score_textview)
        val incorrectScoreView = findViewById<TextView>(R.id.incorrect_score)
        val correctScoreText = findViewById<TextView>(R.id.textView2)
        val incorrectScoreText = findViewById<TextView>(R.id.textView3)

        val restoreData1 : CharSequence = savedInstanceState.getCharSequence("Data restored1").toString()
        val restoreData2 : CharSequence = savedInstanceState.getCharSequence("Data restored2").toString()
        val restoreData3 : CharSequence = savedInstanceState.getCharSequence("Data restored3").toString()
        val restoreData4 : CharSequence = savedInstanceState.getCharSequence("Data restored4").toString()

        correctScoreView.text = restoreData1
        incorrectScoreView.text = restoreData2
        correctScoreText.text = restoreData3
        incorrectScoreText.text = restoreData4

    }

}
