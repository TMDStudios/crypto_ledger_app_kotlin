package com.tmdstudios.cryptoledgerkotlin.ledger

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tmdstudios.cryptoledgerkotlin.R
import com.tmdstudios.cryptoledgerkotlin.adapters.LedgerCoinAdapter
import kotlinx.android.synthetic.main.ledger_fragment.view.*

class LedgerFragment : Fragment() {

    private lateinit var viewModel: LedgerViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.ledger_fragment, container, false)

        val adapter = LedgerCoinAdapter()
        val recyclerView = view.rvLedgerCoins
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(LedgerViewModel::class.java)
        viewModel.getPriceObserver().observe(viewLifecycleOwner, Observer {
                ledgerCoin -> adapter.setData(ledgerCoin)
        })

        sharedPreferences = this.requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        viewModel.apiKey = sharedPreferences.getString("APIKey", "").toString()
        if(viewModel.apiKey.isEmpty()){
            Toast.makeText(requireContext(), "Invalid API Key", Toast.LENGTH_LONG).show()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.apiKey = sharedPreferences.getString("APIKey", "").toString()
        viewModel.makeApiCall()
    }

}