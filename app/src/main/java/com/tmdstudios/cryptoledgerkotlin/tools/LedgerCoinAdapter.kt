package com.tmdstudios.cryptoledgerkotlin.tools

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tmdstudios.cryptoledgerkotlin.databinding.LedgerCoinBinding

class LedgerCoinAdapter(private val activity: Activity): RecyclerView.Adapter<LedgerCoinAdapter.LedgerCoinViewHolder>() {

    inner class LedgerCoinViewHolder(val binding: LedgerCoinBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<LedgerCoin>() {
        override fun areItemsTheSame(oldItem: LedgerCoin, newItem: LedgerCoin): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: LedgerCoin, newItem: LedgerCoin): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var ledgerCoins: List<LedgerCoin>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LedgerCoinViewHolder {
        return LedgerCoinViewHolder(LedgerCoinBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: LedgerCoinViewHolder, position: Int) {
        val ledgerCoin = ledgerCoins[position]
        holder.binding.apply {
            tvLedgerCoinName.text = ledgerCoin.name
            var decimalPointIndex = ledgerCoin.total_amount.indexOf(".") + 9
            val totalAmount = "Total Amount: " + ledgerCoin.total_amount.substring(0, decimalPointIndex)
            tvLedgerCoinAmount.text = totalAmount
            decimalPointIndex = ledgerCoin._purchase_price.indexOf(".") + 3
            val purchasePrice = "$" + ledgerCoin._purchase_price.substring(0, decimalPointIndex)
            tvLedgerCoinPurchasePrice.text = purchasePrice
            decimalPointIndex = ledgerCoin.current_price.indexOf(".") + 3
            val currentPrice = "$" + ledgerCoin.current_price.substring(0, decimalPointIndex)
            tvLedgerCoinPrice.text = currentPrice
            decimalPointIndex = ledgerCoin.total_profit.indexOf(".") + 3
            val profit = "$" + ledgerCoin.total_profit.substring(0, decimalPointIndex)
            when{
                ledgerCoin.total_profit.toFloat() > 0.009 -> tvLedgerProfit.setTextColor(Color.argb(255, 34, 139, 34))
                ledgerCoin.total_profit.toFloat() < -0.009 -> tvLedgerProfit.setTextColor(Color.RED)
                else -> tvLedgerProfit.setTextColor(Color.BLACK)
            }
            tvLedgerProfit.text = profit
            decimalPointIndex = ledgerCoin.total_value.indexOf(".") + 3
            val totalValue = "Total Value: $" + ledgerCoin.total_value.substring(0, decimalPointIndex)
            tvLedgerCoinValue.text = totalValue
            cvLedgerCoin.setOnClickListener {
                CustomAlertDialog(
                        activity,
                        ledgerCoin.name,
                        ledgerCoin.id.toString(),
                        ledgerCoin.total_amount,
                        2
                )
            }
        }
    }

    override fun getItemCount() = ledgerCoins.size

}