package com.tmdstudios.cryptoledgerkotlin.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tmdstudios.cryptoledgerkotlin.R
import com.tmdstudios.cryptoledgerkotlin.models.LedgerCoin
import kotlinx.android.synthetic.main.coin_history_item.view.*

class CoinHistoryAdapter: RecyclerView.Adapter<CoinHistoryAdapter.CHViewHolder>() {
    class CHViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private var coinHistory = emptyList<LedgerCoin>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CHViewHolder {
        return CHViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.coin_history_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CHViewHolder, position: Int) {
        val entry = coinHistory[position]

        holder.itemView.apply {
            var decimalPointIndex: Int
            if(entry.sold){
                val amt = entry.sellAmount
                decimalPointIndex = entry.sellPrice.toString().indexOf(".") + 3
                val price = entry.sellPrice.toString().substring(0, decimalPointIndex)
                decimalPointIndex = entry.totalProfit.toString().indexOf(".") + 3
                val profit = entry.gain.toString().substring(0, decimalPointIndex)
                tvStatusCH.text = "Sold $amt at \$$price"
                tvProfitCH.text = "Profit: \$$profit"
                if(profit.startsWith("-")){
                    tvProfitCH.setTextColor(Color.RED)
                }else{
                    tvProfitCH.setTextColor(Color.rgb(0, 180, 0))
                }
            }else if(entry.merged){
                val amt = entry.amount
                decimalPointIndex = entry.purchasePrice.toString().indexOf(".") + 3
                val price = entry.purchasePrice.toString().substring(0, decimalPointIndex)
                tvStatusCH.text = "Coin Merged"
                tvProfitCH.text = "Last Purchase: $amt at $price"
            }else{
                val amt = entry.totalAmount
                decimalPointIndex = entry.priceDifference.toString().indexOf(".") + 5
                val priceDiff = entry.priceDifference.toString().substring(0, decimalPointIndex)
                tvStatusCH.text = "Total Amount: $amt"
                tvProfitCH.text = "Trend: $priceDiff %"
                if(priceDiff.startsWith("-")){
                    tvProfitCH.setTextColor(Color.RED)
                }else{
                    tvProfitCH.setTextColor(Color.rgb(0, 180, 0))
                }
            }
        }
    }

    override fun getItemCount() = coinHistory.size

    fun setData(ledgerCoins: List<LedgerCoin>){
        this.coinHistory = ledgerCoins
        Log.e("CoinHistoryAdapter", "SET DATA", )
        notifyDataSetChanged()
    }
}