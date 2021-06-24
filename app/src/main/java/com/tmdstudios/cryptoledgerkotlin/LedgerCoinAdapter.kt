package com.tmdstudios.cryptoledgerkotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tmdstudios.cryptoledgerkotlin.databinding.LedgerCoinBinding

class LedgerCoinAdapter: RecyclerView.Adapter<LedgerCoinAdapter.LedgerCoinViewHolder>() {
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
        holder.binding.apply {
            val ledgerCoin = ledgerCoins[position]
            tvLedgerCoinName.text = ledgerCoin.name
            tvLedgerCoinPrice.text = ledgerCoin.current_price
            tvLedgerCoinTrend.text = ledgerCoin.price_difference.toString()
        }
    }

    override fun getItemCount() = ledgerCoins.size

}