package com.tmdstudios.cryptoledgerkotlin.ledger

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tmdstudios.cryptoledgerkotlin.R
import com.tmdstudios.cryptoledgerkotlin.adapters.CoinHistoryAdapter
import kotlinx.android.synthetic.main.fragment_coin_history.view.*

class CoinHistoryFragment : Fragment() {
    private lateinit var viewModel: LedgerViewModel
    private lateinit var progressBar: RelativeLayout
    private lateinit var rvHistoryItems: RecyclerView

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

        progressBar = view.findViewById(R.id.rlLoadingCoinHistory)

        rvHistoryItems = view.rvCoinHistory

        viewModel.sortMethod = "-1"



        viewModel.makeApiCall()

        return view
    }

}