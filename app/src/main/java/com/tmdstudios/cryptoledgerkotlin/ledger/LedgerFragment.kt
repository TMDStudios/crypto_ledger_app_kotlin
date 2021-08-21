package com.tmdstudios.cryptoledgerkotlin.ledger

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tmdstudios.cryptoledgerkotlin.R
import com.tmdstudios.cryptoledgerkotlin.adapters.LedgerCoinAdapter
import kotlinx.android.synthetic.main.home_fragment.view.*
import kotlinx.android.synthetic.main.ledger_fragment.view.*
import kotlinx.android.synthetic.main.prices_fragment.view.*

class LedgerFragment : Fragment() {

    private lateinit var viewModel: LedgerViewModel
    private lateinit var sharedPreferences: SharedPreferences

    private val zoomOpen: Animation by lazy { AnimationUtils.loadAnimation(this.activity, R.anim.open_zoom_anim) }
    private val zoomClose: Animation by lazy { AnimationUtils.loadAnimation(this.activity, R.anim.close_zoom_anim) }
    private val expand: Animation by lazy { AnimationUtils.loadAnimation(this.activity, R.anim.expand_anim) }
    private val retract: Animation by lazy { AnimationUtils.loadAnimation(this.activity, R.anim.retract_anim) }
    private val leftOpen: Animation by lazy { AnimationUtils.loadAnimation(this.activity, R.anim.open_left_anim) }
    private val leftClose: Animation by lazy { AnimationUtils.loadAnimation(this.activity, R.anim.close_left_anim) }

    private lateinit var fabMenu: FloatingActionButton
    private lateinit var fabSort: FloatingActionButton
    private lateinit var fabClear: FloatingActionButton
    private lateinit var etSearch: EditText
    private lateinit var rvCoins: RecyclerView

    private lateinit var progressBar: RelativeLayout

    private var clicked = false

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
        viewModel.checkProgressBar().observe(viewLifecycleOwner, Observer {
            progressBarVisible -> progressBar.isVisible = progressBarVisible
        })

        sharedPreferences = this.requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        viewModel.apiKey = sharedPreferences.getString("APIKey", "").toString()
        if(viewModel.apiKey.isEmpty()){
            Toast.makeText(requireContext(), "Invalid API Key", Toast.LENGTH_LONG).show()
        }

        progressBar = view.rlLoadingLedger

        rvCoins = view.rvLedgerCoins

        fabMenu = view.fabMenuLedger
        fabMenu.setOnClickListener {
            menuButtonClicked()
        }
        fabSort = view.fabSortLedger
        fabSort.setOnClickListener {
            if(viewModel.sortMethod=="Asc"){viewModel.sortMethod="Desc"}else{viewModel.sortMethod="Asc"}
            viewModel.makeApiCall()
            Toast.makeText(this.activity, viewModel.sortMethod, Toast.LENGTH_LONG).show()
        }
        fabClear = view.fabClearLedger
        fabClear.setOnClickListener {
            menuButtonClicked()
            hideKeyboard(this.requireView())
            etSearch.text.clear()
            viewModel.sortMethod = "0"
            viewModel.makeApiCall()
            Toast.makeText(this.activity, "Search cleared", Toast.LENGTH_LONG).show()
        }
        etSearch = view.etSearchLedger
        etSearch.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search()
                    true
                }
                else -> false
            }
        }

        return view
    }

    private fun search(){
        menuButtonClicked()
        hideKeyboard(this.requireView())
        viewModel.sortMethod = etSearch.text.toString()
        viewModel.makeApiCall()
    }

    private fun hideKeyboard(view: View){
        val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
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
            fabClear.visibility = View.VISIBLE
            etSearch.visibility = View.VISIBLE
        }else{
            fabSort.visibility = View.GONE
            fabClear.visibility = View.GONE
            etSearch.visibility = View.GONE
        }
    }

    private fun setAnimation(clicked: Boolean){
        if(!clicked){
            fabMenu.startAnimation(zoomOpen)
            fabSort.startAnimation(expand)
            fabClear.startAnimation(leftOpen)
            etSearch.startAnimation(leftOpen)
            rvCoins.setPadding(8,8,8,128)
        }else{
            fabMenu.startAnimation(zoomClose)
            fabSort.startAnimation(retract)
            fabClear.startAnimation(leftClose)
            etSearch.startAnimation(leftClose)
            rvCoins.setPadding(8,8,8,8)
        }
    }

    private fun setClickable(clicked: Boolean){
        if(!clicked){
            fabSort.isClickable = true
            fabClear.isClickable = true
            etSearch.isClickable = true
        }else{
            fabSort.isClickable = false
            fabClear.isClickable = false
            etSearch.isClickable = false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.apiKey = sharedPreferences.getString("APIKey", "").toString()
        viewModel.makeApiCall()
    }

}