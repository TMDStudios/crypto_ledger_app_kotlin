package com.tmdstudios.cryptoledgerkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tmdstudios.cryptoledgerkotlin.databinding.ActivityHomeBinding
import com.tmdstudios.cryptoledgerkotlin.tools.LedgerCoinAdapter
import com.tmdstudios.cryptoledgerkotlin.tools.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException

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
                RetrofitInstance.api.getLedger("b08d0d5bc719b6b027fd2f9c4332d3ece9f868eb")
            } catch (e: IOException){
                Log.e("Home", "IOException, you might not be connected to the internet", )
                binding.ledgerProgressBar.isVisible = false
                Snackbar.make(binding.root, "IOException, you might not be connected to the internet", Snackbar.LENGTH_LONG).show()
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e("Home", "HTTPException, unexpected response", )
                binding.ledgerProgressBar.isVisible = false
                Snackbar.make(binding.root, "HTTPException, unexpected response", Snackbar.LENGTH_LONG).show()
                return@launchWhenCreated
            }
            if(response.isSuccessful && response.body() != null){
                ledgerCoinAdapter.ledgerCoins = response.body()!!.filter { coin -> !coin.merged }
            }else{
                Log.e("Home", "Response unsuccessful", )
                Snackbar.make(binding.root, "Response unsuccessful", Snackbar.LENGTH_LONG).show()
            }
            binding.ledgerProgressBar.isVisible = false
        }

        title = "My Ledger"
    }

    private fun setupRecyclerView() = binding.rvLedgerCoins.apply {
        ledgerCoinAdapter = LedgerCoinAdapter(this@Home)
        adapter = ledgerCoinAdapter
        layoutManager = LinearLayoutManager(this@Home)
    }
}