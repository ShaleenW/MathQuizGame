package com.example.mobilecw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val newGameBtn = findViewById<Button>(R.id.newgame_bt)
        newGameBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,StartActivity::class.java)
            startActivity(intent)
        })
        val aboutBtn = findViewById<Button>(R.id.about_bt)
        aboutBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,About::class.java)
            startActivity(intent)
        })

    }
}