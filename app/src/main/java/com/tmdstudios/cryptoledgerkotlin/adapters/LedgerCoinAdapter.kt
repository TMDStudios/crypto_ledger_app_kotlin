package com.tmdstudios.cryptoledgerkotlin.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tmdstudios.cryptoledgerkotlin.R
import com.tmdstudios.cryptoledgerkotlin.ledger.LedgerFragmentDirections
import com.tmdstudios.cryptoledgerkotlin.models.LedgerCoin
import kotlinx.android.synthetic.main.ledger_coin.view.*
import java.lang.Exception

class LedgerCoinAdapter: RecyclerView.Adapter<LedgerCoinAdapter.LedgerCoinViewHolder>() {
    class LedgerCoinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private var ledgerCoins = emptyList<LedgerCoin>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LedgerCoinViewHolder {
        return LedgerCoinViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ledger_coin,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LedgerCoinViewHolder, position: Int) {
        val ledgerCoin = ledgerCoins[position]
        holder.itemView.apply {
            tvLedgerCoinName.text = ledgerCoin.name
            val totalAmount = "Total Amount: " + ledgerCoin.total_amount.toString()
            tvLedgerCoinAmount.text = totalAmount
            var decimalPointIndex = ledgerCoin._purchase_price.indexOf(".") + 3
            val purchasePrice = try{
                "$" + ledgerCoin._purchase_price.substring(0, decimalPointIndex)
            }catch(e: Exception){
                "$" + ledgerCoin._purchase_price
            }
            tvLedgerCoinPurchasePrice.text = purchasePrice
            decimalPointIndex = ledgerCoin.current_price.indexOf(".") + 3
            val currentPrice = try{
                "$" + ledgerCoin.current_price.substring(0, decimalPointIndex)
            }catch(e: Exception){
                "$" + ledgerCoin.current_price
            }
            tvLedgerCoinPrice.text = currentPrice
            decimalPointIndex = ledgerCoin.total_profit.indexOf(".") + 3
            val profit = try{
                "$" + ledgerCoin.total_profit.substring(0, decimalPointIndex)
            }catch(e: Exception){
                "$" + ledgerCoin.total_profit
            }
            when{
                ledgerCoin.total_profit.toFloat() > 0.009 -> tvLedgerProfit.setTextColor(Color.argb(255, 34, 139, 34))
                ledgerCoin.total_profit.toFloat() < -0.009 -> tvLedgerProfit.setTextColor(Color.RED)
                else -> tvLedgerProfit.setTextColor(Color.BLACK)
            }
            tvLedgerProfit.text = profit
            decimalPointIndex = ledgerCoin.total_value.indexOf(".") + 3
            val totalValue = try{
                "Total Value: $" + ledgerCoin.total_value.substring(0, decimalPointIndex)
            }catch(e: Exception){
                "Total Value: $" + ledgerCoin.total_value
            }
            tvLedgerCoinValue.text = totalValue
            cvLedgerCoin.setOnClickListener {
                val action = LedgerFragmentDirections.actionLedgerFragmentToSellCoinFragment(ledgerCoin)
                findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount() = ledgerCoins.size

    fun setData(ledgerCoins: List<LedgerCoin>){
        this.ledgerCoins = ledgerCoins
        notifyDataSetChanged()
    }
}