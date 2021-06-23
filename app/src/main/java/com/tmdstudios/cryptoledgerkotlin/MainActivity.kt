package com.tmdstudios.cryptoledgerkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var nextBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nextBtn = findViewById(R.id.btNext)
        nextBtn.setOnClickListener { login() }
    }

    private fun login(){
        val intent = Intent(this,Home::class.java)
        startActivity(intent)
    }
}