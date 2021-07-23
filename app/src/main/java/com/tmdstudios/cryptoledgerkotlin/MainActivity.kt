package com.tmdstudios.cryptoledgerkotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.isVisible
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
    private lateinit var progressBar: RelativeLayout

    private var apiKey = ""
    private var validAPI = false
    private var showApiKey = true

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        apiKey = sharedPreferences.getString("APIKey", "").toString()
        validAPI = sharedPreferences.getBoolean("validAPI", false)

        apiLayout = findViewById(R.id.llAPIHolder)
        apiEntry = findViewById(R.id.etAPIKey)
        apiSubmitButton = findViewById(R.id.btEnterKey)
        apiSubmitButton.setOnClickListener { validateAPI() }

        viewApiButton = findViewById(R.id.btSeeAPI)
        viewApiButton.setOnClickListener {
            showApiKey = !showApiKey
            updateApiVisibility()
        }

        progressBar = findViewById(R.id.rlLoadingMain)

        ledgerBtn = findViewById(R.id.btLedger)
        pricesBtn = findViewById(R.id.btPrices)
        activateButtons(true)

        if(validAPI){
            apiEntry.setText(apiKey)
            showApiKey = false
            updateApiVisibility()
        }

        title = "Crypto Ledger Login"
    }

    private fun validateAPI(){
        apiKey = apiEntry.text.toString()
        progressBar.isVisible = true
        activateButtons(false)
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
                progressBar.isVisible = false
                if(msg=="OK"){
                    validAPI = true
                    activateButtons(true)
                    showApiKey = false
                    updateApiVisibility()
                    with (sharedPreferences.edit()) {
                        putString("APIKey", apiKey)
                        putBoolean("validAPI", validAPI)
                        apply()
                    }
                    showAlert("API Key Accepted")
                }else{
                    apiEntry.text.clear()
                    showAlert("Invalid API Key")
                }
            }
        }
    }

    private fun viewLedger(){
        if(validAPI){
            val intent = Intent(this,Home::class.java)
            updateIntent(intent)
        }else{
            showAlert("Please provide a valid API Key")
        }
    }

    private fun viewPrices(){
        if(validAPI){
            val intent = Intent(this,ViewPrices::class.java)
            updateIntent(intent)
        }else{
            showAlert("Please provide a valid API Key")
        }
    }

    private fun updateIntent(intent: Intent){
        intent.putExtra("APIKey", apiKey)
        intent.putExtra("validAPI", validAPI)
        startActivity(intent)
    }

    private fun showAlert(msg: String){
        CustomAlertDialog(
                this@MainActivity,
                msg,
                "",
                "",
                0
        )
    }

    private fun activateButtons(activate: Boolean){
        if(activate){
            ledgerBtn.setOnClickListener { viewLedger() }
            pricesBtn.setOnClickListener { viewPrices() }
        }else{
            ledgerBtn.setOnClickListener {  }
            pricesBtn.setOnClickListener {  }
        }
    }

    private fun updateApiVisibility(){
        if(showApiKey){
            apiLayout.isVisible = true
            viewApiButton.text = "Hide API Key"
        }else{
            apiLayout.isVisible = false
            viewApiButton.text = "View API Key"
        }
    }
}