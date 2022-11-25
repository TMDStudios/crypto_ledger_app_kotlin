package com.tmdstudios.cryptoledgerkotlin.ledger

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmdstudios.cryptoledgerkotlin.api.RetrofitInstance
import com.tmdstudios.cryptoledgerkotlin.models.LedgerCoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class LedgerViewModel : ViewModel() {
    var priceData: MutableLiveData<List<LedgerCoin>> = MutableLiveData()
    var apiKey = ""
    var sortMethod = "0"
    var showProgressBar: MutableLiveData<Boolean> = MutableLiveData()
    var coinSold: MutableLiveData<Boolean> = MutableLiveData()
    var coinHistoryId = ""

    init {
        makeApiCall()
    }

    fun checkProgressBar(): MutableLiveData<Boolean>{
        return showProgressBar
    }

    fun getPriceObserver(): MutableLiveData<List<LedgerCoin>> {
        return priceData
    }

    fun isCoinSold(): MutableLiveData<Boolean>{
        return coinSold
    }

    fun makeApiCall(){
        viewModelScope.launch(Dispatchers.IO) {
            showProgressBar.postValue(true)
            Log.e("Ledger", "key $apiKey", )
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
                    Log.e("Ledger", "Got the data! ${response.body()}", )
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
                    }else if(sortMethod=="-1"){
                        Log.e("VIEW MODEL", "coinHistoryName = $coinHistoryId", )

                        priceData.postValue(response.body()!!
                            .filter { coin -> coin.name.contains(coinHistoryId) }
                            .sortedByDescending { coin -> coin.id })
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
            delay(500L)
            showProgressBar.postValue(false)
        }
    }

    fun sellCoin(coinID: Int, amt: Double){
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType: MediaType? = "application/x-www-form-urlencoded".toMediaTypeOrNull()
            val body: RequestBody = RequestBody.create(
                mediaType,
                "id=$coinID&amount=$amt"
            )
            val request: Request = Request.Builder()
                .url("https://cls-prod-cls-z2mvyu.mo1.mogenius.io/api/sell/$apiKey")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cookie", "JSESSIONID=376B885635F4795460A6BD770D4C02D4")
                .build()
            val response: Response = client.newCall(request).execute()
            Log.e("ViewPrices", "Response: $response")
        }
    }
}