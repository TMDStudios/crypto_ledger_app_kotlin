package com.tmdstudios.cryptoledgerkotlin.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tmdstudios.cryptoledgerkotlin.R
import com.tmdstudios.cryptoledgerkotlin.models.Coin
import com.tmdstudios.cryptoledgerkotlin.prices.PricesFragmentDirections
import kotlinx.android.synthetic.main.coin.view.*

class CoinAdapter(private val fManager: FragmentManager): RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {
    class CoinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private var coins = emptyList<Coin>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        return CoinViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.coin,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {

        holder.itemView.apply {
            val coin = coins[position]
            val coinName = coin.name + " (" + coin.symbol + ")"
            tvCoinName.text = coinName
            var decimalPointIndex = coin.price.indexOf(".") + 5
            val price = "$" + coin.price.substring(0, decimalPointIndex)
            tvCoinPrice.text = price
            decimalPointIndex = coin.price_1h.toString().indexOf(".") + 2
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
                val action = PricesFragmentDirections.actionPricesFragmentToBuyCoinFragment(coin)
                findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount() = coins.size

    fun setData(coins: List<Coin>){
        this.coins = coins
        notifyDataSetChanged()
    }

}