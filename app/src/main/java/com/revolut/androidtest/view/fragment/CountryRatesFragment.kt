package com.revolut.androidtest.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.revolut.androidtest.CountryRatesModule
import com.revolut.androidtest.R
import com.revolut.androidtest.api.APIClient
import com.revolut.androidtest.view.model.CountryRatesViewModel

class CountryRatesFragment : Fragment() {

    private lateinit var viewModel: CountryRatesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = CountryRatesModule.create(APIClient().getClient()).getViewModelFor(this, CountryRatesViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_country_rates, container, false)
    }
}