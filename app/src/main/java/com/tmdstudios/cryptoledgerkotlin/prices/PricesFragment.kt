package com.tmdstudios.cryptoledgerkotlin.prices

import android.graphics.Color
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
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tmdstudios.cryptoledgerkotlin.R
import com.tmdstudios.cryptoledgerkotlin.adapters.CoinAdapter
import kotlinx.android.synthetic.main.ledger_fragment.view.*
import kotlinx.android.synthetic.main.prices_fragment.*
import kotlinx.android.synthetic.main.prices_fragment.view.*

class PricesFragment : Fragment() {

    private lateinit var viewModel: PricesViewModel

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
    private lateinit var clBottom: ConstraintLayout

    private var clicked = false

    private lateinit var webView: WebView

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
        viewModel.getPriceObserver().observe(viewLifecycleOwner, Observer {
                coin -> adapter.setData(coin)
        })
        viewModel.checkProgressBar().observe(viewLifecycleOwner, Observer {
                progressBarVisible -> progressBar.isVisible = progressBarVisible
        })

        progressBar = view.rlLoadingPrices
        clBottom = view.clBottomPrices

        rvCoins = view.rvCoins

        fabMenu = view.fabMenu
        fabMenu.setOnClickListener {
            menuButtonClicked()
        }
        fabSort = view.fabSort
        fabSort.setOnClickListener {
            if(viewModel.sortMethod=="Asc"){viewModel.sortMethod="Desc"}else{viewModel.sortMethod="Asc"}
            viewModel.makeApiCall()
            Toast.makeText(this.activity, viewModel.sortMethod, Toast.LENGTH_LONG).show()
        }
        fabClear = view.fabClear
        fabClear.setOnClickListener {
            menuButtonClicked()
            hideKeyboard(this.requireView())
            etSearch.text.clear()
            viewModel.sortMethod = "Asc"
            viewModel.makeApiCall()
            Toast.makeText(this.activity, "Search cleared", Toast.LENGTH_LONG).show()
        }
        etSearch = view.etSearch
        etSearch.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search()
                    true
                }
                else -> false
            }
        }

        webView = view.findViewById(R.id.webViewPrices)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.loadUrl("https://cryptoledger.pythonanywhere.com/api/docs/")

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
            clBottom.setBackgroundColor(Color.argb(223,255,255,255))
        }else{
            fabSort.visibility = View.GONE
            fabClear.visibility = View.GONE
            etSearch.visibility = View.GONE
            clBottom.setBackgroundColor(Color.argb(0,0,0,0))
        }
    }

    private fun setAnimation(clicked: Boolean){
        if(!clicked){
            fabMenu.startAnimation(zoomOpen)
            fabSort.startAnimation(expand)
            fabClear.startAnimation(leftOpen)
            etSearch.startAnimation(leftOpen)
        }else{
            fabMenu.startAnimation(zoomClose)
            fabSort.startAnimation(retract)
            fabClear.startAnimation(leftClose)
            etSearch.startAnimation(leftClose)
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

}