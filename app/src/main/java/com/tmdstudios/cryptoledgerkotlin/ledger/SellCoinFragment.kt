package com.tmdstudios.cryptoledgerkotlin.ledger

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tmdstudios.cryptoledgerkotlin.R
import kotlinx.android.synthetic.main.fragment_sell_coin.view.*
import java.lang.Exception

class SellCoinFragment : Fragment() {

    private val args by navArgs<SellCoinFragmentArgs>()

    private lateinit var viewModel: LedgerViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sell_coin, container, false)

        viewModel = ViewModelProvider(this).get(LedgerViewModel::class.java)

        viewModel.isCoinSold().observe(viewLifecycleOwner, Observer {
            coinSold -> run {
            if(coinSold){
                viewModel.coinSold.postValue(false)
                val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                Toast.makeText(requireContext(),
                    "${view.etSellAmount.text} ${args.currentLedgerCoin.name} sold!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_sellCoinFragment_to_ledgerFragment)
            }
        }
        })

        sharedPreferences = this.requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        viewModel.apiKey = sharedPreferences.getString("APIKey", "").toString()

        val coinID = args.currentLedgerCoin.id
        val owned = args.currentLedgerCoin.totalAmount.toFloat()

        view.tvSellCoinName.text = args.currentLedgerCoin.name
        var decimalPointIndex = args.currentLedgerCoin.currentPrice.toString().indexOf(".") + 3
        val currentPrice = try{
            "$ " + args.currentLedgerCoin.currentPrice.toString().substring(0, decimalPointIndex)
        }catch(e: Exception){
            "$ " + args.currentLedgerCoin.currentPrice.toString()
        }
        view.tvSellCoinPrice.text = currentPrice
        decimalPointIndex = args.currentLedgerCoin.purchasePrice.toString().indexOf(".") + 3
        val purchasePrice = try{
            "$ " + args.currentLedgerCoin.purchasePrice.toString().substring(0, decimalPointIndex)
        }catch(e: Exception){
            "$ " + args.currentLedgerCoin.purchasePrice.toString()
        }
        view.tvSellCoinPurchasePrice.text = purchasePrice
        decimalPointIndex = args.currentLedgerCoin.priceDifference.toString().indexOf(".") + 3
        val trend = try{
            args.currentLedgerCoin.priceDifference.toString().substring(0, decimalPointIndex) + " %"
        }catch(e: Exception){
            args.currentLedgerCoin.priceDifference.toString() + " %"
        }
        view.tvSellCoinTrend.text = trend
        when{
            args.currentLedgerCoin.priceDifference > 0.009 -> view.tvSellCoinTrend.setTextColor(Color.argb(255, 34, 139, 34))
            args.currentLedgerCoin.priceDifference < -0.009 -> view.tvSellCoinTrend.setTextColor(Color.RED)
            else -> view.tvSellCoinTrend.text = "0.00 %"
        }
        decimalPointIndex = args.currentLedgerCoin.totalAmount.toString().indexOf(".") + 9
        val amtOwned = try{
            args.currentLedgerCoin.totalAmount.toString().substring(0, decimalPointIndex)
        }catch(e: Exception){
            args.currentLedgerCoin.totalAmount.toString()
        }
        view.tvSellCoinAmountOwned.text = amtOwned
        decimalPointIndex = args.currentLedgerCoin.totalProfit.toString().indexOf(".") + 3
        val totalProfit = try{
            "$ " + args.currentLedgerCoin.totalProfit.toString().substring(0, decimalPointIndex)
        }catch(e: Exception){
            "$ " + args.currentLedgerCoin.totalProfit.toString()
        }
        view.tvSellCoinTotalProfit.text = totalProfit
        when{
            args.currentLedgerCoin.totalProfit.toFloat() > 0.009 -> view.tvSellCoinTotalProfit.setTextColor(Color.argb(255, 34, 139, 34))
            args.currentLedgerCoin.totalProfit.toFloat() < -0.009 -> view.tvSellCoinTotalProfit.setTextColor(Color.RED)
            else -> view.tvSellCoinTotalProfit.text = "$ 0.00"
        }

        view.btSellCoinSubmit.setOnClickListener {
            val amount = if (view.etSellAmount.text.toString().isEmpty()){
                0.0
            } else{
                view.etSellAmount.text.toString().toDouble()
            }
            if(amount > 0.0 && amount <= owned){
                viewModel.sellCoin(coinID, amount.toDouble())
            }else{
                view.etSellAmount.setText("0")
                Toast.makeText(requireContext(), "Invalid Amount", Toast.LENGTH_LONG).show()
            }
        }

        view.btViewHistory.setOnClickListener {
            with(sharedPreferences.edit()) {
                putString("coinHistoryId", args.currentLedgerCoin.name)
                apply()
            }
            findNavController().navigate(R.id.coinHistoryFragment)
        }

        return view
    }

}