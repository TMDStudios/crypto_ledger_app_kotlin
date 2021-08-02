package com.tmdstudios.cryptoledgerkotlin.home

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tmdstudios.cryptoledgerkotlin.api.RetrofitInstance
import com.tmdstudios.cryptoledgerkotlin.models.LedgerCoin
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.lang.IllegalStateException

class HomeViewModel : ViewModel() {
    var apiKey = ""
    var showApiKey = true
    var validAPI: MutableLiveData<Boolean> = MutableLiveData()
    var apiChecked = false
    var tickerData = ""

    fun isAPIValid(): MutableLiveData<Boolean> {
        return validAPI
    }

    init {
        updateTicker()
    }

    fun validateAPI(api: String, sharedPreferences: SharedPreferences){
        apiKey = api
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
                apiChecked = true
                if(msg=="OK"){
                    validAPI.postValue(true)
                    showApiKey = false
                    with (sharedPreferences.edit()) {
                        putString("APIKey", apiKey)
                        putBoolean("validAPI", isAPIValid().value!!)
                        apply()
                    }
                    Log.e("HomeViewModel", "API Key accepted!", )
                }else{
                    validAPI.postValue(false)
                    showApiKey = true
                    with (sharedPreferences.edit()) {
                        putString("APIKey", "")
                        putBoolean("validAPI", isAPIValid().value!!)
                        apply()
                    }
                    Log.e("HomeViewModel", "Invalid API Key", )
                }
            }
        }
    }

    fun updateTicker(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = try {
                RetrofitInstance.api.getPrices()
            } catch (e: IOException){
                e.toString()
                return@launch
            } catch (e: HttpException) {
                e.toString()
                return@launch
            }
            if(response.isSuccessful && response.body() != null){
                for(i in response.body()!!){
                    val decimalPoint = i.price.indexOf(".")
                    val coin = " ${i.symbol} \$${i.price.substring(0, decimalPoint + 3)}"
                    tickerData += coin
                }
                Log.e("HomeViewModel", "data: $tickerData", )
            }else{
                Log.e("ViewPrices", "Unable to get prices", )
            }
        }
    }
}