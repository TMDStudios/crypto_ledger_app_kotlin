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
        view.tvBuyCoinName.text = coinName
        var decimalPointIndex = args.currentCoin.price.indexOf(".") + 5
        val price = "$ " + args.currentCoin.price.substring(0, decimalPointIndex)
        view.tvBuyCoinPrice.text = price
        decimalPointIndex = args.currentCoin.price_1h.toString().indexOf(".") + 5
        val price1h = try{
            args.currentCoin.price_1h.toString().substring(0, decimalPointIndex) + " %"
        }catch(e: StringIndexOutOfBoundsException){
            "0 %"
        }
        view.tvBuyCoinPrice1h.text = price1h
        when {
            args.currentCoin.price_1h<0 -> {view.tvBuyCoinPrice1h.setTextColor(Color.RED)}
            args.currentCoin.price_1h>0 -> {view.tvBuyCoinPrice1h.setTextColor(Color.argb(255, 34, 139, 34))}
            else -> {view.tvBuyCoinPrice1h.setTextColor(Color.argb(255, 48, 48, 48))}
        }
        decimalPointIndex = args.currentCoin.price_24h.toString().indexOf(".") + 5
        val price24h = args.currentCoin.price_24h.toString().substring(0, decimalPointIndex) + " %"
        view.tvBuyCoinPrice24h.text = price24h
        when {
            args.currentCoin.price_24h<0 -> {view.tvBuyCoinPrice24h.setTextColor(Color.RED)}
            args.currentCoin.price_24h>0 -> {view.tvBuyCoinPrice24h.setTextColor(Color.argb(255, 34, 139, 34))}
            else -> {view.tvBuyCoinPrice24h.setTextColor(Color.argb(255, 48, 48, 48))}
        }
        view.tvBuyCoinPriceBTC.text = args.currentCoin.price_btc
        view.tvBuyCoinPriceETH.text = args.currentCoin.price_eth

        view.btBuyCoinSubmit.setOnClickListener {
            if(view.etBuyCustomPrice.text.toString().isNotEmpty()){
                if(view.etBuyAmount.text.toString().isNotEmpty()){
                    val amt = view.etBuyAmount.text.toString().toFloat()
                    val customPrice = view.etBuyCustomPrice.text.toString()
                    viewModel.buyCoin(coinName, amt, customPrice)
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
                    val amt = view.etBuyAmount.text.toString().toFloat()
                    viewModel.buyCoin(coinName, amt, "0")
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