package com.tmdstudios.cryptoledgerkotlin.ledger

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmdstudios.cryptoledgerkotlin.api.RetrofitInstance
import com.tmdstudios.cryptoledgerkotlin.models.LedgerCoin
import com.tmdstudios.cryptoledgerkotlin.models.SellCoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class LedgerViewModel : ViewModel() {
    var priceData: MutableLiveData<List<LedgerCoin>> = MutableLiveData()
    var apiKey = ""
    var sortMethod = "0"
    var showProgressBar = true

    init {
        makeApiCall()
    }

    fun getPriceObserver(): MutableLiveData<List<LedgerCoin>> {
        return priceData
    }

    fun makeApiCall(){
        viewModelScope.launch(Dispatchers.IO) {
            showProgressBar = true
            if(apiKey.isNotEmpty()){
                val response = try {
                    RetrofitInstance.api.getLedger(apiKey)
                }catch (e: IOException){
                    Log.e("Ledger", "IOException, you might not be connected to the internet", )
                    return@launch
                }catch (e: HttpException){
                    Log.e("Ledger", "HTTPException, unexpected response", )
                    return@launch
                }
                if(response.isSuccessful && response.body() != null){
                    showProgressBar = false
                    Log.e("Ledger", "Got the data! ${response.body()}", )
//                    priceData.postValue(response.body()!!
//                        .filter { coin -> !coin.merged && !coin.sold }
//                        .sortedByDescending { coin -> coin.id })
                    if(sortMethod=="0"){
                        priceData.postValue(response.body()!!
                            .filter { coin -> !coin.merged && !coin.sold }
                            .sortedByDescending { coin -> coin.id })
                    }else if(sortMethod=="Asc"){
                        priceData.postValue(response.body()!!
                            .filter { coin -> !coin.merged && !coin.sold }
                            .sortedBy { coin -> coin.name })
                    }else if(sortMethod=="Desc"){
                        priceData.postValue(response.body()!!
                            .filter { coin -> !coin.merged && !coin.sold }
                            .sortedByDescending { coin -> coin.name })
                    }else{
                        priceData.postValue(response.body()!!
                            .filter { coin -> !coin.merged && !coin.sold }
                            .filter { coin -> coin.name.toUpperCase(Locale.ROOT).contains(sortMethod.toUpperCase(Locale.ROOT)) }
                            .sortedBy { coin -> coin.name })
                    }
                }else{
                    Log.e("Ledger", "Unable to get prices", )

                }
            }else{
                Log.e("Ledger", "Invalid API Key", )
            }
        }
    }

    fun sellCoin(coinID: Int, amt: Float){
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.sellCoin(apiKey, SellCoin(coinID, amt))
            }catch (e: IOException){
                Log.e("SellCoin", "IOException, you might not be connected to the internet", )
                return@launch
            }catch (e: HttpException){
                Log.e("SellCoin", "HTTPException, unexpected response", )
                return@launch
            }
            if(response.isSuccessful && response.body() != null){
                Log.e("SellCoin", "Coin SOLD", )
                makeApiCall()
            }else{
                Log.e("SellCoin", "ISSUE! id: $coinID, amt: $amt, response: $response", )
            }
        }
    }
}