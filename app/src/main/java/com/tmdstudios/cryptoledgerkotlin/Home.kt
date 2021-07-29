package com.tmdstudios.cryptoledgerkotlin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tmdstudios.cryptoledgerkotlin.databinding.ActivityHomeBinding
import com.tmdstudios.cryptoledgerkotlin.tools.CustomAlertDialog
import com.tmdstudios.cryptoledgerkotlin.tools.LedgerCoinAdapter
import com.tmdstudios.cryptoledgerkotlin.tools.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var ledgerCoinAdapter: LedgerCoinAdapter
    private lateinit var refreshBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var ticker: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        refreshBtn = findViewById(R.id.btRefreshLedger)
        refreshBtn.setOnClickListener { this.recreate() }

        backBtn = findViewById(R.id.btBackLedger)
        backBtn.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        ticker = findViewById(R.id.tvTicker)
        ticker.isSelected = true
        ticker.text = "You are logged in with the following API Key: ${intent.getStringExtra("APIKey").toString()}"

        if(intent.getBooleanExtra("bought", false)){
            showAlert("Coin Bought")
            intent.putExtra("bought", false)
        }

        lifecycleScope.launchWhenCreated {
            binding.rlLoadingLedger.isVisible = true
            val response = try {
                RetrofitInstance.api.getLedger(intent.getStringExtra("APIKey").toString())
            } catch (e: IOException){
                Log.e("Home", "IOException, you might not be connected to the internet", )
                binding.rlLoadingLedger.isVisible = false
                Snackbar.make(binding.root, "IOException, you might not be connected to the internet", Snackbar.LENGTH_LONG).show()
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e("Home", "HTTPException, unexpected response", )
                binding.rlLoadingLedger.isVisible = false
                Snackbar.make(binding.root, "HTTPException, unexpected response", Snackbar.LENGTH_LONG).show()
                return@launchWhenCreated
            }
            if(response.isSuccessful && response.body() != null){
                ledgerCoinAdapter.ledgerCoins = response.body()!!
                        .filter { coin -> !coin.merged && !coin.sold }
                        .sortedByDescending { coin -> coin.id }
            }else{
                Log.e("Home", "Response unsuccessful", )
                Snackbar.make(binding.root, "Response unsuccessful", Snackbar.LENGTH_LONG).show()
            }
            binding.rlLoadingLedger.isVisible = false
        }

        title = "My Ledger"
    }

    private fun setupRecyclerView() = binding.rvLedgerCoins.apply {
        ledgerCoinAdapter = LedgerCoinAdapter(this@Home)
        adapter = ledgerCoinAdapter
        layoutManager = LinearLayoutManager(this@Home)
    }

    fun confirmSale(success: Boolean){
        if(success){
            showAlert("Coin Sold")
        }else{
            showAlert("Invalid Amount")
        }
    }

    private fun showAlert(title: String){
        CustomAlertDialog(
                this,
                title,
                "",
                "",
                0
        )
    }
}