package com.tmdstudios.cryptoledgerkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var nextBtn: Button
    lateinit var pricesBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nextBtn = findViewById(R.id.btNext)
        nextBtn.setOnClickListener { login() }

        pricesBtn = findViewById(R.id.btPrices)
        pricesBtn.setOnClickListener { prices() }
    }

    private fun login(){
        val intent = Intent(this,Home::class.java)
        startActivity(intent)
    }

    private fun prices(){
        val intent = Intent(this,ViewPrices::class.java)
        startActivity(intent)
    }
}