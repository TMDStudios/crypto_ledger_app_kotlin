package com.tmdstudios.cryptoledgerkotlin.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tmdstudios.cryptoledgerkotlin.ViewPrices
import com.tmdstudios.cryptoledgerkotlin.databinding.CoinBinding

class CoinAdapter(private val activity: Activity) : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

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

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.binding.apply {
            val coin = coins[position]
            val coinName = coin.name + " (" + coin.symbol + ")"
            tvCoinName.text = coinName
            tvCoinPrice.text = coin.price
            var decimalPointIndex = coin.price_1h.toString().indexOf(".") + 2
            val price1h = "1h: " + coin.price_1h.toString().substring(0, decimalPointIndex) + "%"
            tvPrice1h.text = price1h
            when {
                coin.price_1h<0 -> {tvPrice1h.setTextColor(Color.RED)}
                coin.price_1h>0 -> {tvPrice1h.setTextColor(Color.argb(255, 34, 139, 34))}
                else -> {tvPrice1h.setTextColor(Color.argb(255, 48, 48, 48))}
            }
            decimalPointIndex = coin.price_24h.toString().indexOf(".") + 2
            val price24h = "24h: " + coin.price_24h.toString().substring(0, decimalPointIndex) + "%"
            tvPrice24h.text = price24h
            when {
                coin.price_24h<0 -> {tvPrice24h.setTextColor(Color.RED)}
                coin.price_24h>0 -> {tvPrice24h.setTextColor(Color.argb(255, 34, 139, 34))}
                else -> {tvPrice24h.setTextColor(Color.argb(255, 48, 48, 48))}
            }
            tvPriceBTC.text = coin.price_btc
            tvPriceETH.text = coin.price_eth
            cvCoin.setOnClickListener {
                if((activity as ViewPrices).validAPI){
                    CustomAlertDialog(activity, coinName, "", "", 1)
                }
            }
        }
    }

    override fun getItemCount() = coins.size
}