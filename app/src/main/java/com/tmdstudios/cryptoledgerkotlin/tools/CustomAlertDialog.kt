package com.tmdstudios.cryptoledgerkotlin.tools

import android.app.Activity
import android.content.DialogInterface
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.tmdstudios.cryptoledgerkotlin.Home
import com.tmdstudios.cryptoledgerkotlin.ViewPrices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CustomAlertDialog(val activity: Activity, title: String, message: String, private val alertType: Int) {
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
        dialogBuilder.setMessage(message)
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    run {
                        if (alertType == 1) {
                            println("BUY COIN: $title")
                            val price = if(customPrice.text.toString().isEmpty()){
                                0f
                            }else{
                                customPrice.text.toString().toFloat()
                            }
                            updateLedger(
                                    title,
                                    amount.text.toString().toFloat(),
                                    price)
                        } else {
                            activity.recreate()
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

    private fun updateLedger(coin: String, amt: Float, price: Float){
        if(alertType == 1){
            CoroutineScope(IO).launch {
                async { buyCoin(coin, amt, price) }.await()
                println("COIN ORDER COMPLETE: $coin, amt: $amt")
                (activity as ViewPrices).goHome()
            }
        }
    }

    private suspend fun buyCoin(coin: String, amt: Float, price: Float){
        RetrofitInstance.api.buyCoin("b08d0d5bc719b6b027fd2f9c4332d3ece9f868eb", BuyCoin(
                coin,
                amt,
                price
        ))
    }
}