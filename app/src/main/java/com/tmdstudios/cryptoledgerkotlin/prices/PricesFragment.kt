package com.tmdstudios.cryptoledgerkotlin.prices

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tmdstudios.cryptoledgerkotlin.R
import com.tmdstudios.cryptoledgerkotlin.adapters.CoinAdapter
import kotlinx.android.synthetic.main.prices_fragment.*
import kotlinx.android.synthetic.main.prices_fragment.view.*

class PricesFragment : Fragment() {

    private lateinit var viewModel: PricesViewModel

    private val zoomOpen: Animation by lazy { AnimationUtils.loadAnimation(this.activity, R.anim.open_zoom_anim) }
    private val zoomClose: Animation by lazy { AnimationUtils.loadAnimation(this.activity, R.anim.close_zoom_anim) }
    private val expand: Animation by lazy { AnimationUtils.loadAnimation(this.activity, R.anim.expand_anim) }
    private val retract: Animation by lazy { AnimationUtils.loadAnimation(this.activity, R.anim.retract_anim) }

    private lateinit var fabMenu: FloatingActionButton
    private lateinit var fabSort: FloatingActionButton
    private lateinit var fabSearch: FloatingActionButton

    private var clicked = false

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


        fabMenu = view.fabMenu
        fabMenu.setOnClickListener {
            menuButtonClicked()
        }
        fabSort = view.fabSort
        fabSort.setOnClickListener {
            Toast.makeText(this.activity, "Sorting", Toast.LENGTH_LONG).show()
        }
        fabSearch = view.fabSearch
        fabSearch.setOnClickListener {
            Toast.makeText(this.activity, "Searching", Toast.LENGTH_LONG).show()
        }

        return view
    }

    private fun menuButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean){
        if(!clicked){
            fabSort.visibility = View.VISIBLE
            fabSearch.visibility = View.VISIBLE
        }else{
            fabSort.visibility = View.GONE
            fabSearch.visibility = View.GONE
        }
    }

    private fun setAnimation(clicked: Boolean){
        if(!clicked){
            fabMenu.startAnimation(zoomOpen)
            fabSort.startAnimation(expand)
            fabSearch.startAnimation(expand)
        }else{
            fabMenu.startAnimation(zoomClose)
            fabSort.startAnimation(retract)
            fabSearch.startAnimation(retract)
        }
    }

    private fun setClickable(clicked: Boolean){
        if(!clicked){
            fabSort.isClickable = true
            fabSearch.isClickable = true
        }else{
            fabSort.isClickable = false
            fabSearch.isClickable = false
        }
    }

}