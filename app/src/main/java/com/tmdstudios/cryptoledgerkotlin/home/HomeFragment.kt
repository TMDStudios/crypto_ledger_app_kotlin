package com.tmdstudios.cryptoledgerkotlin.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.tmdstudios.cryptoledgerkotlin.R
import kotlinx.android.synthetic.main.home_fragment.view.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var apiLayout: LinearLayout
    private lateinit var apiEntry: EditText
    private lateinit var apiSubmitButton: Button
    private lateinit var viewApiButton: Button
    private lateinit var progressBar: RelativeLayout
    private lateinit var ticker: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        sharedPreferences = this.requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        viewModel.apiKey = sharedPreferences.getString("APIKey", "").toString()
        viewModel.validAPI = sharedPreferences.getBoolean("validAPI", false)

        apiLayout = view.llAPIHolder
        apiEntry = view.etAPIKey
        apiSubmitButton = view.btEnterKey
        apiSubmitButton.setOnClickListener {
            viewModel.validateAPI(apiEntry.text.toString(), sharedPreferences)
        }

        viewApiButton = view.btSeeAPI
        viewApiButton.setOnClickListener {
            viewModel.showApiKey = !viewModel.showApiKey
            updateApiVisibility()
        }

        progressBar = view.rlLoadingMain

        ticker = view.tvTicker
        ticker.isSelected = true

        if(viewModel.validAPI){
            apiEntry.setText(viewModel.apiKey)
            viewModel.showApiKey = false
            updateApiVisibility()
        }

        return view
    }

    private fun updateApiVisibility(){
        if(viewModel.showApiKey){
            apiLayout.isVisible = true
            viewApiButton.text = "Hide API Key"
        }else{
            apiLayout.isVisible = false
            viewApiButton.text = "View API Key"
        }
        ticker.text = "You are logged in with the following API Key: ${viewModel.apiKey}"
    }

}