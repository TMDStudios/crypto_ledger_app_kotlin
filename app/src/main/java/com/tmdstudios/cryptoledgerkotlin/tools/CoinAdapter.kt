package com.tmdstudios.cryptoledgerkotlin.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tmdstudios.cryptoledgerkotlin.databinding.CoinBinding

class CoinAdapter : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    inner class CoinViewHolder(val binding: CoinBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Coin>() {
        override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var coins: List<Coin>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        return CoinViewHolder(CoinBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.binding.apply {
            val coin = coins[position]
            tvCoinName.text = coin.name
            tvCoinPrice.text = coin.price
            tvCoinTrend.text = coin.price_1h.toString()
        }
    }

    override fun getItemCount() = coins.size
}