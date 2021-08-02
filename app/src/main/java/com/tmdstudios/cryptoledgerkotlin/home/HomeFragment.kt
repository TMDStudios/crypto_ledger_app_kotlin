package com.tmdstudios.cryptoledgerkotlin.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
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
    private lateinit var getAPIKeyButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.isAPIValid().observe(viewLifecycleOwner, Observer {
            if(viewModel.apiChecked){
                if(viewModel.isAPIValid().value!!){
                    Toast.makeText(requireContext(), "API Key Accepted", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(requireContext(), "Invalid API Key", Toast.LENGTH_LONG).show()
                }
            }
        })

        sharedPreferences = this.requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        viewModel.apiKey = sharedPreferences.getString("APIKey", "").toString()
        viewModel.validAPI.postValue(sharedPreferences.getBoolean("validAPI", false))

        apiLayout = view.llAPIHolder
        apiEntry = view.etAPIKey
        apiSubmitButton = view.btEnterKey
        apiSubmitButton.setOnClickListener {
            viewModel.validateAPI(apiEntry.text.toString(), sharedPreferences)
            // Hide Keyboard
            val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        viewApiButton = view.btSeeAPI
        viewApiButton.setOnClickListener {
            viewModel.showApiKey = !viewModel.showApiKey
            updateApiVisibility(viewModel.showApiKey)
        }

        getAPIKeyButton = view.btGetAPIKey
        getAPIKeyButton.setOnClickListener {
            val url = "https://crypto-ledger.herokuapp.com"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        progressBar = view.rlLoadingMain

        ticker = view.tvTicker
        ticker.isSelected = true

        if(viewModel.apiKey.isNotEmpty()){
            viewModel.validateAPI(viewModel.apiKey, sharedPreferences)
            viewModel.showApiKey = false
            updateApiVisibility(viewModel.showApiKey)
        }

        return view
    }

    private fun updateApiVisibility(show: Boolean){
        if(show){
            apiEntry.setText(viewModel.apiKey)
            apiLayout.isVisible = true
            getAPIKeyButton.isVisible = true
            viewApiButton.text = "Hide API Key"
        }else{
            apiLayout.isVisible = false
            getAPIKeyButton.isVisible = false
            viewApiButton.text = "View API Key"
        }
        ticker.text = "You are logged in with the following API Key: ${viewModel.apiKey}"
    }

}