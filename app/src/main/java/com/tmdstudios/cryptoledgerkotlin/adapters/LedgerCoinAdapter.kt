package com.tmdstudios.cryptoledgerkotlin.adapters

import android.graphics.Color
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
            val totalAmount = "Total Amount: " + ledgerCoin.totalAmount.toString()
            tvLedgerCoinAmount.text = totalAmount
            var decimalPointIndex = ledgerCoin.purchasePrice.toString().indexOf(".") + 3
            val purchasePrice = try{
                "$" + ledgerCoin.purchasePrice.toString().substring(0, decimalPointIndex)
            }catch(e: Exception){
                "$" + ledgerCoin.purchasePrice.toString()
            }
            tvLedgerCoinPurchasePrice.text = purchasePrice
            decimalPointIndex = ledgerCoin.currentPrice.toString().indexOf(".") + 3
            val currentPrice = try{
                "$" + ledgerCoin.currentPrice.toString().substring(0, decimalPointIndex)
            }catch(e: Exception){
                "$" + ledgerCoin.currentPrice.toString()
            }
            tvLedgerCoinPrice.text = currentPrice
            decimalPointIndex = ledgerCoin.totalProfit.toString().indexOf(".") + 3
            val profit = try{
                "$" + ledgerCoin.totalProfit.toString().substring(0, decimalPointIndex)
            }catch(e: Exception){
                "$" + ledgerCoin.totalProfit.toString()
            }
            when{
                ledgerCoin.totalProfit.toFloat() > 0.009 -> tvLedgerProfit.setTextColor(Color.argb(255, 34, 139, 34))
                ledgerCoin.totalProfit.toFloat() < -0.009 -> tvLedgerProfit.setTextColor(Color.RED)
                else -> tvLedgerProfit.setTextColor(Color.BLACK)
            }
            tvLedgerProfit.text = profit
            decimalPointIndex = ledgerCoin.totalValue.toString().indexOf(".") + 3
            val totalValue = try{
                "Total Value: $" + ledgerCoin.totalValue.toString().substring(0, decimalPointIndex)
            }catch(e: Exception){
                "Total Value: $" + ledgerCoin.totalValue.toString()
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