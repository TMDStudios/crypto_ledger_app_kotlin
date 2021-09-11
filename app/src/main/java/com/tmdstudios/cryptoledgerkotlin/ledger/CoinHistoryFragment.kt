package com.tmdstudios.cryptoledgerkotlin.ledger

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tmdstudios.cryptoledgerkotlin.R
import com.tmdstudios.cryptoledgerkotlin.adapters.CoinHistoryAdapter
import kotlinx.android.synthetic.main.coin.view.*
import kotlinx.android.synthetic.main.fragment_coin_history.view.*

class CoinHistoryFragment : Fragment() {
    private lateinit var viewModel: LedgerViewModel
    private lateinit var progressBar: RelativeLayout
    private lateinit var rvHistoryItems: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tvNameCH: TextView
    private lateinit var fabBackCH: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_coin_history, container, false)

        val adapter = CoinHistoryAdapter()
        val recyclerView = view.rvCoinHistory
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(LedgerViewModel::class.java)
        viewModel.checkProgressBar().observe(viewLifecycleOwner, Observer {
                progressBarVisible -> progressBar.isVisible = progressBarVisible
        })

        viewModel.getPriceObserver().observe(viewLifecycleOwner, Observer {
                ledgerCoin -> adapter.setData(ledgerCoin)
        })

        progressBar = view.findViewById(R.id.rlLoadingCoinHistory)

        rvHistoryItems = view.rvCoinHistory

        sharedPreferences = this.requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        viewModel.apiKey = sharedPreferences.getString("APIKey", "").toString()

        tvNameCH = view.tvNameCH

        fabBackCH = view.fabBackCH
        fabBackCH.setOnClickListener {
            findNavController().navigate(R.id.action_coinHistoryFragment_to_ledgerFragment)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.sortMethod = "-1"
        viewModel.coinHistoryId = sharedPreferences.getString("coinHistoryId", "").toString()
        tvNameCH.text = viewModel.coinHistoryId
        viewModel.makeApiCall()
    }

}