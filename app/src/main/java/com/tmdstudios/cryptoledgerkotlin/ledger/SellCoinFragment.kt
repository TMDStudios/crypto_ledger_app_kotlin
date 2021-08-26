package com.tmdstudios.cryptoledgerkotlin.ledger

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
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
import kotlinx.android.synthetic.main.coin.view.*
import kotlinx.android.synthetic.main.fragment_sell_coin.*
import kotlinx.android.synthetic.main.fragment_sell_coin.view.*
import kotlinx.android.synthetic.main.ledger_coin.view.*

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
        val owned = args.currentLedgerCoin.total_amount.toFloat()

        view.tvSellCoinName.text = args.currentLedgerCoin.name
        var decimalPointIndex = args.currentLedgerCoin.current_price.indexOf(".") + 3
        val currentPrice = "$ " + args.currentLedgerCoin.current_price.substring(0, decimalPointIndex)
        view.tvSellCoinPrice.text = currentPrice
        decimalPointIndex = args.currentLedgerCoin._purchase_price.indexOf(".") + 3
        val purchasePrice = "$ " + args.currentLedgerCoin._purchase_price.substring(0, decimalPointIndex)
        view.tvSellCoinPurchasePrice.text = purchasePrice
        decimalPointIndex = args.currentLedgerCoin.price_difference.toString().indexOf(".") + 3
        val trend = args.currentLedgerCoin.price_difference.toString().substring(0, decimalPointIndex) + " %"
        view.tvSellCoinTrend.text = trend
        when{
            args.currentLedgerCoin.price_difference > 0.009 -> view.tvSellCoinTrend.setTextColor(Color.argb(255, 34, 139, 34))
            args.currentLedgerCoin.price_difference < -0.009 -> view.tvSellCoinTrend.setTextColor(Color.RED)
            else -> view.tvSellCoinTrend.setTextColor(Color.BLACK)
        }
        decimalPointIndex = args.currentLedgerCoin.total_amount.indexOf(".") + 9
        val amtOwned = args.currentLedgerCoin.total_amount.substring(0, decimalPointIndex)
        view.tvSellCoinAmountOwned.text = amtOwned
        decimalPointIndex = args.currentLedgerCoin.total_profit.indexOf(".") + 3
        val totalProfit = "$ " + args.currentLedgerCoin.total_profit.substring(0, decimalPointIndex)
        view.tvSellCoinTotalProfit.text = totalProfit
        when{
            args.currentLedgerCoin.total_profit.toFloat() > 0.009 -> view.tvSellCoinTotalProfit.setTextColor(Color.argb(255, 34, 139, 34))
            args.currentLedgerCoin.total_profit.toFloat() < -0.009 -> view.tvSellCoinTotalProfit.setTextColor(Color.RED)
            else -> view.tvSellCoinTotalProfit.setTextColor(Color.BLACK)
        }

        view.btSellCoinSubmit.setOnClickListener {
            val amount = if (view.etSellAmount.text.toString().isEmpty()){
                0f
            } else{
                view.etSellAmount.text.toString().toFloat()
            }
//            Log.e("SellCoinFragment", "ISSUE! amt: $coinID, amt: $amount, owned: $owned", )
            if(amount > 0f && amount <= owned){
                viewModel.sellCoin(coinID, amount)
            }else{
                view.etSellAmount.setText("0")
                Toast.makeText(requireContext(), "Invalid Amount", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

}