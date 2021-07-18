package com.tmdstudios.cryptoledgerkotlin.tools

import android.app.Activity
import android.content.DialogInterface
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.tmdstudios.cryptoledgerkotlin.Home
import com.tmdstudios.cryptoledgerkotlin.R
import com.tmdstudios.cryptoledgerkotlin.ViewPrices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CustomAlertDialog(
        private val activity: Activity,
        title: String,
        coinId: String,
        totalAmount: String,
        private val alertType: Int) {
//    Alert Types: 1 = buy coin
    init {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(activity)

//        Add Linear Layout
        val layout = LinearLayout(activity)
        layout.orientation = LinearLayout.VERTICAL

        // Set up the input
        val amount = EditText(activity)
        val customPrice = EditText(activity)
        // Specify the type of input expected
        amount.hint = "Enter Amount"
        amount.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        customPrice.hint = "Custom Price (optional)"
        customPrice.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        layout.addView(amount)
        if(alertType==1){layout.addView(customPrice)}


    // set message of alert dialog
        dialogBuilder.setMessage("Some text here")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    run {
                        val amt = amount.text.toString().toFloat()
                        when(alertType){
                            1 -> {
                                    println("BUY COIN: $title")
                                    val price = if(customPrice.text.toString().isEmpty()){
                                        0f
                                    }else{
                                        customPrice.text.toString().toFloat()
                                    }

                                    CoroutineScope(IO).launch {
                                        async { buyCoin(title, amt, price) }.await()
                                        println("COIN ORDER COMPLETE: $title, amt: $amt")
                                        (activity as ViewPrices).goHome()
                                    }
                                }
                            2 -> {
                                    println("SELL COIN: $title")
                                    CoroutineScope(IO).launch {
                                        async { sellCoin(coinId.toInt(), amt) }.await()
                                        println("COIN SALE COMPLETE: $title, amt: $amt, id: $coinId")
                                        withContext(Main){activity.recreate()}
                                    }
                                }
                            else -> activity.recreate()
                        }
                    }
                })
                // negative button text and action
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        when(alertType){
            1 -> alert.setTitle("Buy $title")
            2 -> alert.setTitle("Sell $title")
            else -> alert.setTitle(title)
        }
        // show alert dialog
        alert.setView(layout)
        alert.show()
    }

    private suspend fun buyCoin(coin: String, amt: Float, price: Float){
        RetrofitInstance.api.buyCoin("b08d0d5bc719b6b027fd2f9c4332d3ece9f868eb", BuyCoin(
                coin,
                amt,
                price
        ))
    }

    private suspend fun sellCoin(id: Int, amount: Float){
        RetrofitInstance.api.sellCoin("b08d0d5bc719b6b027fd2f9c4332d3ece9f868eb", SellCoin(
                id,
                amount
        ))
    }
}