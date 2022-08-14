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
            decimalPointIndex = coin.priceChangePercentage1d.toString().indexOf(".") + 2
            val price1d = "1d: " + coin.priceChangePercentage1d.toString().substring(0, decimalPointIndex) + "%"
            tvPrice1d.text = price1d
            when {
                coin.priceChangePercentage1d<0 -> {tvPrice1d.setTextColor(Color.RED)}
                coin.priceChangePercentage1d>0 -> {tvPrice1d.setTextColor(Color.argb(255, 34, 139, 34))}
                else -> {tvPrice1d.setTextColor(Color.argb(255, 48, 48, 48))}
            }
            decimalPointIndex = coin.priceChangePercentage7d.toString().indexOf(".") + 2
            val price7d = "7d: " + coin.priceChangePercentage7d.toString().substring(0, decimalPointIndex) + "%"
            tvPrice7d.text = price7d
            when {
                coin.priceChangePercentage7d<0 -> {tvPrice7d.setTextColor(Color.RED)}
                coin.priceChangePercentage7d>0 -> {tvPrice7d.setTextColor(Color.argb(255, 34, 139, 34))}
                else -> {tvPrice7d.setTextColor(Color.argb(255, 48, 48, 48))}
            }

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