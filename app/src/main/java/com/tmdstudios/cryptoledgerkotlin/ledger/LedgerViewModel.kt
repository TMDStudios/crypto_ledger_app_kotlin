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

class LedgerViewModel : ViewModel() {
    var priceData: MutableLiveData<List<LedgerCoin>> = MutableLiveData()
    var apiKey = ""

    init {
        makeApiCall()
    }

    fun getPriceObserver(): MutableLiveData<List<LedgerCoin>> {
        return priceData
    }

    fun makeApiCall(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getLedger(apiKey)
            }catch (e: IOException){
                Log.e("ViewPrices", "IOException, you might not be connected to the internet", )
                return@launch
            }catch (e: HttpException){
                Log.e("ViewPrices", "HTTPException, unexpected response", )
                return@launch
            }
            if(response.isSuccessful && response.body() != null){
                Log.e("ViewPrices", "Got the data! ${response.body()}", )
                priceData.postValue(response.body()!!
                    .filter { coin -> !coin.merged && !coin.sold }
                    .sortedByDescending { coin -> coin.id })
            }else{
                Log.e("ViewPrices", "Unable to get prices", )

            }
        }
    }

    fun sellCoin(coinID: Int, amt: Float){
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.sellCoin(apiKey, SellCoin(coinID, amt))
            }catch (e: IOException){
                Log.e("ViewPrices", "IOException, you might not be connected to the internet", )
                return@launch
            }catch (e: HttpException){
                Log.e("ViewPrices", "HTTPException, unexpected response", )
                return@launch
            }
            if(response.isSuccessful && response.body() != null){
                Log.e("ViewPrices", "Coin SOLD", )
                makeApiCall()
            }else{
                Log.e("SellCoin", "ISSUE! id: $coinID, amt: $amt, response: $response", )
            }
        }
    }
}