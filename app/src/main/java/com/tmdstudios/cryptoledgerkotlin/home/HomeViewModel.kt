package com.tmdstudios.cryptoledgerkotlin.home

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.tmdstudios.cryptoledgerkotlin.api.RetrofitInstance
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.lang.IllegalStateException

class HomeViewModel : ViewModel() {
    var apiKey = ""
    var validAPI = false
    var showApiKey = true

    fun validateAPI(api: String, sharedPreferences: SharedPreferences){
        apiKey = api
//        progressBar.isVisible = true
//        activateButtons(false)
        CoroutineScope(Dispatchers.IO).launch {
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
            withContext(Dispatchers.Main){
//                progressBar.isVisible = false
                if(msg=="OK"){
                    validAPI = true
                    showApiKey = false
//                    updateApiVisibility()
                    with (sharedPreferences.edit()) {
                        putString("APIKey", apiKey)
                        putBoolean("validAPI", validAPI)
                        apply()
                    }
                    Log.e("HomeViewModel", "API Key accepted!", )
//                    showAlert("API Key Accepted")
                }else{
                    Log.e("HomeViewModel", "Invalid API Key", )
//                    apiEntry.text.clear()
//                    showAlert("Invalid API Key")
                }
            }
        }
    }
}