package com.example.mobilecw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val okBtn = findViewById<Button>(R.id.ok_bt)
        okBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@About,MainActivity::class.java)
            startActivity(intent)
        })
    }
}