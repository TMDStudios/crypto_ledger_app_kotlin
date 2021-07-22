package com.tmdstudios.cryptoledgerkotlin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.tmdstudios.cryptoledgerkotlin.tools.CustomAlertDialog
import com.tmdstudios.cryptoledgerkotlin.tools.RetrofitInstance
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.HttpException
import java.io.IOException
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {
    private lateinit var apiLayout: LinearLayout
    private lateinit var apiEntry: EditText
    private lateinit var apiSubmitButton: Button
    private lateinit var viewApiButton: Button
    private lateinit var ledgerBtn: Button
    private lateinit var pricesBtn: Button

    private var apiKey = ""
    private var validAPI = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiEntry = findViewById(R.id.etAPIKey)

        ledgerBtn = findViewById(R.id.btLedger)
        ledgerBtn.setOnClickListener { viewLedger() }

        pricesBtn = findViewById(R.id.btPrices)
        pricesBtn.setOnClickListener { viewPrices() }

        title = "Crypto Ledger Login"
    }

    private fun validateAPI(){
        apiKey = apiEntry.text.toString()
        CoroutineScope(IO).launch {
            val msg: String = async {
                try {
                    RetrofitInstance.api.getLedger(apiKey).message()
                } catch (e: IOException){
                    e.toString()
                } catch (e: HttpException) {
                    e.toString()
                } catch (e: IllegalStateException) {
                    e.toString()
                }
            }.await()
            withContext(Main){
                if(msg=="OK"){
                    validAPI = true
                }else{
                    apiEntry.text.clear()
                    CustomAlertDialog(
                            this@MainActivity,
                            "Invalid API Key",
                            "",
                            "",
                            0
                    )
                }
            }
        }
    }

    private fun viewLedger(){
        validateAPI()
        if(validAPI){
            val intent = Intent(this,Home::class.java)
            intent.putExtra("APIKey", apiKey)
            intent.putExtra("validAPI", validAPI)
            startActivity(intent)
        }
    }

    private fun viewPrices(){
        validateAPI()
        if(validAPI){
            val intent = Intent(this,ViewPrices::class.java)
            intent.putExtra("APIKey", apiKey)
            intent.putExtra("validAPI", validAPI)
            startActivity(intent)
        }
    }
}