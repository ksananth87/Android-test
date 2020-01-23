package com.revolut.androidtest.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolut.androidtest.CountryRatesModule
import com.revolut.androidtest.R
import com.revolut.androidtest.api.APIClient
import com.revolut.androidtest.view.adapter.CountryListAdapter
import com.revolut.androidtest.view.model.CurrencyList
import com.revolut.androidtest.view.viewmodel.CountryRatesViewModel
import kotlinx.android.synthetic.main.fragment_country_rates.*

class CountryRatesFragment : Fragment() {

    private lateinit var viewModel: CountryRatesViewModel
    private val countryListAdapter = CountryListAdapter {
        viewModel.currencyClicked(it)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = CountryRatesModule.create(APIClient().getClient())
            .getViewModelFor(this, CountryRatesViewModel::class.java)
        setupObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_country_rates, container, false)

        viewModel.fragmentLoaded()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fragmentLoaded()
        }

        with(countryList) {
            setHasFixedSize(true)
            adapter = countryListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        viewModel.showProgressDialog().observe(this, Observer {
            updateSwipeRefresh(it)
        })
        viewModel.getRates().observe(this, Observer {
            updateCountryList(it)
        })

        viewModel.getError().observe(this, Observer {
            showErrorScreen(it)
        })

        viewModel.moveCurrencyToTop().observe(this, Observer {
            swapCountry(it)
        })
    }

    private fun showErrorScreen(it: Boolean?) {
        countryList.visibility = View.GONE
        error.visibility = View.VISIBLE
    }

    private fun updateSwipeRefresh(progressStatus: Boolean) {
        swipeRefreshLayout.isRefreshing = progressStatus
    }

    private fun updateCountryList(rates: CurrencyList?) {
        countryListAdapter.setItems(rates!!.currencyList)
        //countryListAdapter.setBase(rates)
        //viewModel.refreshRatesEveryOneSec()
    }

    private fun swapCountry(clickedPos: Int) {
        countryListAdapter.moveItem(clickedPos, 0)
        countryList.scrollToPosition(0)
    }
}