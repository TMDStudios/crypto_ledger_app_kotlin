package com.tmdstudios.cryptoledgerkotlin.prices

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tmdstudios.cryptoledgerkotlin.R
import kotlinx.android.synthetic.main.coin.view.*
import kotlinx.android.synthetic.main.fragment_buy_coin.view.*
import kotlinx.android.synthetic.main.fragment_sell_coin.view.*
import java.lang.Exception

class BuyCoinFragment : Fragment() {

    private val args by navArgs<BuyCoinFragmentArgs>()

    private lateinit var viewModel: PricesViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_buy_coin, container, false)

        viewModel = ViewModelProvider(this).get(PricesViewModel::class.java)

        sharedPreferences = this.requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        viewModel.apiKey = sharedPreferences.getString("APIKey", "").toString()
        if(viewModel.apiKey.isEmpty()){
            Toast.makeText(requireContext(), "Valid API Key needed to buy coins", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_buyCoinFragment_to_pricesFragment)
        }

        val coinName = args.currentCoin.name + " (" + args.currentCoin.symbol + ")"
        val coinSymbol = args.currentCoin.symbol
        view.tvBuyCoinName.text = coinName
        var decimalPointIndex = args.currentCoin.price.indexOf(".") + 5
        val price = try{
            "$ " + args.currentCoin.price.substring(0, decimalPointIndex)
        }catch(e: Exception){
            "$ " + args.currentCoin.price
        }
        view.tvBuyCoinPrice.text = price
        decimalPointIndex = args.currentCoin.priceChangePercentage1d.toString().indexOf(".") + 5
        var priceChange = args.currentCoin.priceChangePercentage1d*100
        val price1d = try{
            priceChange.toString().substring(0, decimalPointIndex) + " %"
        }catch(e: Exception){
            "$priceChange %"
        }
        view.tvBuyCoinPrice1d.text = price1d
        when {
            args.currentCoin.priceChangePercentage1d<0 -> {view.tvBuyCoinPrice1d.setTextColor(Color.RED)}
            args.currentCoin.priceChangePercentage1d>0 -> {view.tvBuyCoinPrice1d.setTextColor(Color.argb(255, 34, 139, 34))}
            else -> {view.tvBuyCoinPrice1d.setTextColor(Color.argb(255, 48, 48, 48))}
        }
        decimalPointIndex = args.currentCoin.priceChangePercentage7d.toString().indexOf(".") + 5
        priceChange = args.currentCoin.priceChangePercentage7d*100
        val price7d = try{
            priceChange.toString().substring(0, decimalPointIndex) + " %"
        }catch(e: Exception){
            priceChange.toString() + " %"
        }
        view.tvBuyCoinPrice7d.text = price7d
        when {
            args.currentCoin.priceChangePercentage7d<0 -> {view.tvBuyCoinPrice7d.setTextColor(Color.RED)}
            args.currentCoin.priceChangePercentage7d>0 -> {view.tvBuyCoinPrice7d.setTextColor(Color.argb(255, 34, 139, 34))}
            else -> {view.tvBuyCoinPrice7d.setTextColor(Color.argb(255, 48, 48, 48))}
        }
        decimalPointIndex = args.currentCoin.priceChangePercentage30d.toString().indexOf(".") + 5
        priceChange = args.currentCoin.priceChangePercentage30d*100
        val price30d = try{
            priceChange.toString().substring(0, decimalPointIndex) + " %"
        }catch(e: Exception){
            priceChange.toString() + " %"
        }
        view.tvBuyCoinPrice30d.text = price30d
        when {
            args.currentCoin.priceChangePercentage30d<0 -> {view.tvBuyCoinPrice30d.setTextColor(Color.RED)}
            args.currentCoin.priceChangePercentage30d>0 -> {view.tvBuyCoinPrice30d.setTextColor(Color.argb(255, 34, 139, 34))}
            else -> {view.tvBuyCoinPrice30d.setTextColor(Color.argb(255, 48, 48, 48))}
        }
//        view.tvBuyCoinPriceBTC.text = args.currentCoin.price_btc
//        view.tvBuyCoinPriceETH.text = args.currentCoin.price_eth

        view.btBuyCoinSubmit.setOnClickListener {
            if(view.etBuyCustomPrice.text.toString().isNotEmpty()){
                if(view.etBuyAmount.text.toString().isNotEmpty()){
                    val amt = view.etBuyAmount.text.toString().toDouble()
                    val customPrice = view.etBuyCustomPrice.text.toString()
                    viewModel.buyCoin(coinName, coinSymbol, amt, customPrice.toDouble())
                    // Hide Keyboard
                    val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    Toast.makeText(requireContext(),
                        "${view.etBuyAmount.text} $coinName bought!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_buyCoinFragment_to_pricesFragment)
                }else{
                    Toast.makeText(requireContext(), "Invalid Amount", Toast.LENGTH_LONG).show()
                    view.etBuyAmount.setText("0")
                }
            }else{
                if(view.etBuyAmount.text.toString().isNotEmpty()){
                    val amt = view.etBuyAmount.text.toString().toDouble()
                    viewModel.buyCoin(coinName, coinSymbol, amt, 0.0)
                    // Hide Keyboard
                    val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    Toast.makeText(requireContext(),
                        "${view.etBuyAmount.text} $coinName bought!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_buyCoinFragment_to_pricesFragment)
                }else{
                    Toast.makeText(requireContext(), "Invalid Amount", Toast.LENGTH_LONG).show()
                    view.etBuyAmount.setText("0")
                }
            }
        }

        return view
    }

}