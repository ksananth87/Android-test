package com.revolut.androidtest.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.revolut.androidtest.CountryRatesModule
import com.revolut.androidtest.R
import com.revolut.androidtest.api.APIClient
import com.revolut.androidtest.domain.Rates
import com.revolut.androidtest.view.viewmodel.CountryRatesViewModel
import kotlinx.android.synthetic.main.fragment_country_rates.*

class CountryRatesFragment : Fragment() {

    private lateinit var viewModel: CountryRatesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = CountryRatesModule.create(APIClient().getClient()).getViewModelFor(this, CountryRatesViewModel::class.java)
        setupObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_country_rates, container, false)

        viewModel.fetchRates()

        return view
    }

    private fun setupObservers() {
        viewModel.showProgressDialog().observe(this, Observer {
            updateSwipeRefresh(it)
        })
        viewModel.getRates().observe(this, Observer {
            updateCountryList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateSwipeRefresh(progressStatus: Boolean) {
        swipeRefreshLayout.isRefreshing = progressStatus
    }

    private fun updateCountryList(rates: Rates?) {

    }
}