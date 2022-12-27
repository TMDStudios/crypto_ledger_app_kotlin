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
                val amt = entry.sell_amount
                decimalPointIndex = entry.sell_price.indexOf(".") + 3
                val price = entry.sell_price.substring(0, decimalPointIndex)
                decimalPointIndex = entry.total_profit.indexOf(".") + 3
                val profit = entry.total_profit.substring(0, decimalPointIndex)
                tvStatusCH.text = "Sold $amt at \$$price"
                tvProfitCH.text = "Profit: \$$profit"
                if(profit.startsWith("-")){
                    tvProfitCH.setTextColor(Color.RED)
                }else{
                    tvProfitCH.setTextColor(Color.rgb(0, 180, 0))
                }
            }else if(entry.merged){
                val amt = entry.amount
                decimalPointIndex = entry._purchase_price.indexOf(".") + 3
                val price = entry._purchase_price.substring(0, decimalPointIndex)
                tvStatusCH.text = "Coin Merged"
                tvProfitCH.text = "Last Purchase: $amt at $price"
            }else{
                val amt = entry.total_amount
                decimalPointIndex = entry.price_difference.toString().indexOf(".") + 5
                var priceDiff = "0"
                try {
                    priceDiff = entry.price_difference.toString().substring(0, decimalPointIndex)

                }catch(e: StringIndexOutOfBoundsException){

                }
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