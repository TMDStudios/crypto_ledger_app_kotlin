package com.tmdstudios.cryptoledgerkotlin.ledger

import android.content.Context
import android.content.SharedPreferences
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
        viewModel.getPriceObserver().observe(viewLifecycleOwner, Observer {
            // Hide Keyboard
            val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
            Toast.makeText(requireContext(),
                "${view.etSellAmount.text} ${args.currentLedgerCoin.name} sold!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_sellCoinFragment_to_ledgerFragment)
        })

        sharedPreferences = this.requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        viewModel.apiKey = sharedPreferences.getString("APIKey", "").toString()

        val coinID = args.currentLedgerCoin.id
        val owned = args.currentLedgerCoin.total_amount.toFloat()

        view.tvSellCoinName.text = args.currentLedgerCoin.name
        view.tvSellCoinPrice.text = args.currentLedgerCoin.current_price
        view.tvSellCoinPurchasePrice.text = args.currentLedgerCoin._purchase_price
        view.tvSellCoinTrend.text = args.currentLedgerCoin.price_difference.toString()
        view.tvSellCoinAmountOwned.text = args.currentLedgerCoin.total_amount
        view.tvSellCoinTotalProfit.text = args.currentLedgerCoin.total_profit

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