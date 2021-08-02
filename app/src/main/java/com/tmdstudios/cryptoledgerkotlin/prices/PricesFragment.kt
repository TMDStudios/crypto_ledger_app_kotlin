package com.tmdstudios.cryptoledgerkotlin.prices

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tmdstudios.cryptoledgerkotlin.R
import com.tmdstudios.cryptoledgerkotlin.adapters.CoinAdapter
import kotlinx.android.synthetic.main.prices_fragment.view.*

class PricesFragment : Fragment() {

    private lateinit var viewModel: PricesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.prices_fragment, container, false)

        val adapter = CoinAdapter(childFragmentManager)
        val recyclerView = view.rvCoins
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(PricesViewModel::class.java)
        viewModel.getPriceObserver().observe(viewLifecycleOwner, Observer { coin -> adapter.setData(coin) })

        return view
    }

}