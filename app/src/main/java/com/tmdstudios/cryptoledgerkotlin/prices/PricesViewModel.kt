package com.tmdstudios.cryptoledgerkotlin.prices

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmdstudios.cryptoledgerkotlin.api.RetrofitInstance
import com.tmdstudios.cryptoledgerkotlin.models.BuyCoin
import com.tmdstudios.cryptoledgerkotlin.models.Coin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class PricesViewModel : ViewModel() {
    var priceData: MutableLiveData<List<Coin>> = MutableLiveData()
    var apiKey = ""
    var sortMethod = "Asc"

    init {
        makeApiCall()
    }

    fun getPriceObserver(): MutableLiveData<List<Coin>>{
        return priceData
    }

    fun makeApiCall(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getPrices()
            }catch (e: IOException){
                Log.e("ViewPrices", "IOException, you might not be connected to the internet", )
                return@launch
            }catch (e: HttpException){
                Log.e("ViewPrices", "HTTPException, unexpected response", )
                return@launch
            }
            if(response.isSuccessful && response.body() != null){
                Log.e("ViewPrices", "Got the data! ${response.message()}", )
                Log.e("ViewPrices", "******** ${response.body()}", )
                if(sortMethod=="Asc"){
                    priceData.postValue(response.body()!!.sortedBy { coin -> coin.name })
                }else if(sortMethod=="Desc"){
                    priceData.postValue(response.body()!!.sortedByDescending { coin -> coin.name })
                }else{
                    priceData.postValue(response.body()!!
                        .filter { coin -> coin.name.toUpperCase(Locale.ROOT).contains(sortMethod.toUpperCase(Locale.ROOT)) ||
                                coin.symbol.contains(sortMethod.toUpperCase(Locale.ROOT)) }
                        .sortedBy { coin -> coin.name })
                }

            }else{
                Log.e("ViewPrices", "Unable to get prices", )
            }
        }
    }

    fun buyCoin(name: String, amount: Float, custom_price: Float){
        if(apiKey.isNotEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                val response = try {
                    RetrofitInstance.api.buyCoin(apiKey, BuyCoin(name, amount, custom_price))
                }catch (e: IOException){
                    Log.e("ViewPrices", "IOException, you might not be connected to the internet", )
                    return@launch
                }catch (e: HttpException){
                    Log.e("ViewPrices", "HTTPException, unexpected response", )
                    return@launch
                }
                if(response.isSuccessful && response.body() != null){
                    Log.e("ViewPrices", "Coin BOUGHT!", )
                }else{
                    Log.e("BuyCoin", "ISSUE! coin: $name, amt: $amount, price: $custom_price, response: $response", )
                }

            }
        }else{
            Log.e("ViewPrices", "Invalid API Key", )
        }
    }
}