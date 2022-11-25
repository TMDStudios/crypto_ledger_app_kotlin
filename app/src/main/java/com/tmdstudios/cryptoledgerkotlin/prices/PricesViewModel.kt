package com.tmdstudios.cryptoledgerkotlin.prices

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmdstudios.cryptoledgerkotlin.api.RetrofitInstance
import com.tmdstudios.cryptoledgerkotlin.models.Coin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import okhttp3.RequestBody.Companion.create

class PricesViewModel : ViewModel() {
    var priceData: MutableLiveData<List<Coin>> = MutableLiveData()
    var apiKey = ""
    var sortMethod = "Asc"
    var showProgressBar: MutableLiveData<Boolean> = MutableLiveData()

    init {
        makeApiCall()
    }

    fun checkProgressBar(): MutableLiveData<Boolean>{
        return showProgressBar
    }

    fun getPriceObserver(): MutableLiveData<List<Coin>>{
        return priceData
    }

    fun makeApiCall(){
        viewModelScope.launch(Dispatchers.IO) {
            showProgressBar.postValue(true)
            val response = try {
                RetrofitInstance.api.getPrices()
            }catch (e: IOException){
                Log.e("ViewPrices", "IOException, you might not be connected to the internet")
                return@launch
            }catch (e: HttpException){
                Log.e("ViewPrices", "HTTPException, unexpected response")
                return@launch
            }
            if(response.isSuccessful && response.body() != null){
                Log.e("ViewPrices", "Got the data! ${response.message()}")
                Log.e("ViewPrices", "******** ${response.body()}")
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
                Log.e("ViewPrices", "Unable to get prices")
            }
            delay(500L)
            showProgressBar.postValue(false)
        }
    }

    fun buyCoin(name: String, symbol: String, amount: Double, purchasePrice: Double){
        if(apiKey.isNotEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                val client = OkHttpClient().newBuilder()
                    .build()
                val mediaType: MediaType? = "application/x-www-form-urlencoded".toMediaTypeOrNull()
                val body: RequestBody = create(
                    mediaType,
                    "name=$name&symbol=$symbol&amount=$amount&purchasePrice=$purchasePrice"
                )
                val request: Request = Request.Builder()
                    .url("https://cls-prod-cls-z2mvyu.mo1.mogenius.io/api/buy/$apiKey")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Cookie", "JSESSIONID=376B885635F4795460A6BD770D4C02D4")
                    .build()
                val response: Response = client.newCall(request).execute()
                Log.e("ViewPrices", "Response: $response")
            }
        }else{
            Log.e("ViewPrices", "Invalid API Key")
        }
    }
}