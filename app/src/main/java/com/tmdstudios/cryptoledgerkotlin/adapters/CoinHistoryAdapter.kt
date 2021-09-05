package com.tmdstudios.cryptoledgerkotlin.adapters

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
            tvBuySellCH.text = "Testing buy"
            tvDateCH.text = entry.date_bought
            tvAmountCH.text = entry.amount
            tvPriceCH.text = entry._purchase_price
        }
    }

    override fun getItemCount() = coinHistory.size
}