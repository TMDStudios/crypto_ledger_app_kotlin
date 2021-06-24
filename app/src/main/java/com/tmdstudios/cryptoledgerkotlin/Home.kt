package com.tmdstudios.cryptoledgerkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tmdstudios.cryptoledgerkotlin.databinding.ActivityHomeBinding
import retrofit2.HttpException
import java.io.IOException

const val TAG = "Home"

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var ledgerCoinAdapter: LedgerCoinAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            binding.ledgerProgressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getLedger()
            } catch (e: IOException){
                Log.e(TAG, "IOException, you might not be connected to the internet", )
                binding.ledgerProgressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HTTPException, unexpected response", )
                binding.ledgerProgressBar.isVisible = false
                return@launchWhenCreated
            }
            if(response.isSuccessful && response.body() != null){
                ledgerCoinAdapter.ledgerCoins = response.body()!!
            }else{
                Log.e(TAG, "Response unsuccessful", )
            }
            binding.ledgerProgressBar.isVisible = false
        }
    }

    private fun setupRecyclerView() = binding.rvLedgerCoins.apply {
        ledgerCoinAdapter = LedgerCoinAdapter()
        adapter = ledgerCoinAdapter
        layoutManager = LinearLayoutManager(this@Home)
    }
}