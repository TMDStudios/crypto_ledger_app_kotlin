package com.tmdstudios.cryptoledgerkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tmdstudios.cryptoledgerkotlin.databinding.ActivityViewPricesBinding
import com.tmdstudios.cryptoledgerkotlin.tools.CoinAdapter
import com.tmdstudios.cryptoledgerkotlin.tools.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException

class ViewPrices : AppCompatActivity() {
    private lateinit var binding: ActivityViewPricesBinding
    private lateinit var coinAdapter: CoinAdapter
    private lateinit var refreshBtn: Button

    var validAPI = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPricesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        validAPI = intent.getBooleanExtra("validAPI", false)

        refreshBtn = findViewById(R.id.btRefreshPrices)
        refreshBtn.setOnClickListener { this.recreate() }

        lifecycleScope.launchWhenCreated {
            binding.coinProgressBar.isVisible = true
            val response = try{
                RetrofitInstance.api.getPrices()
            }catch (e: IOException){
                Log.e("ViewPrices", "IOException, you might not be connected to the internet", )
                binding.coinProgressBar.isVisible = false
                Snackbar.make(binding.root, "IOException, you might not be connected to the internet", Snackbar.LENGTH_LONG).show()
                return@launchWhenCreated
            }catch (e: HttpException){
                Log.e("ViewPrices", "HTTPException, unexpected response", )
                binding.coinProgressBar.isVisible = false
                Snackbar.make(binding.root, "HTTPException, unexpected response", Snackbar.LENGTH_LONG).show()
                return@launchWhenCreated
            }
            if(response.isSuccessful && response.body() != null){
                coinAdapter.coins = response.body()!!.sortedBy { coin -> coin.name }
            }else{
                Log.e("ViewPrices", "Unable to get prices", )
                Snackbar.make(binding.root, "Unable to get prices", Snackbar.LENGTH_LONG).show()
            }
            binding.coinProgressBar.isVisible = false
        }

        title = "All Prices"
    }

    private fun setupRecyclerView() = binding.rvCoins.apply {
        coinAdapter = CoinAdapter(this@ViewPrices)
        adapter = coinAdapter
        layoutManager = LinearLayoutManager(this@ViewPrices)
    }

    fun goHome(){
        val intent = Intent(this,Home::class.java)
        intent.putExtra("bought", true)
        startActivity(intent)
    }
}