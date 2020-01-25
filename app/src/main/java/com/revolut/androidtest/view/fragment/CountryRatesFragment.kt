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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_country_rates.*
import java.util.concurrent.TimeUnit

class CountryRatesFragment : Fragment() {

    private var disposable: Disposable? = null

    private lateinit var viewModel: CountryRatesViewModel
    private lateinit var countryListAdapter:CountryListAdapter

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

        countryListAdapter = CountryListAdapter { position, currency, enteredAmount -> viewModel.currencyValueUpdated(
            currency,
            enteredAmount,
            countryListAdapter.getItems()
        ) }

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
        viewModel.getRefreshedRates().observe(this, Observer {
            updateRefreshedCurrencyList(it)
        })

        viewModel.getError().observe(this, Observer {
            showErrorScreen(it)
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
            disposable = Observable.interval(4, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewModel.refreshCurrencyRates(countryListAdapter.getItems()) }) { this.handleRefreshError(it) }

    }

    private fun handleRefreshError(it: Throwable?) {
        //Do nothing, as we are calling in background
    }

    private fun updateRefreshedCurrencyList(rates: CurrencyList){
        countryListAdapter.updateList(rates.currencyList)
    }

    override fun onResume() {
        super.onResume()
        countryListAdapter.getItems()
    }
}