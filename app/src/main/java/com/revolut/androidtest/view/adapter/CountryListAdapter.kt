package com.revolut.androidtest.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.revolut.androidtest.R
import com.revolut.androidtest.domain.Country
import com.revolut.androidtest.domain.Rates
import com.revolut.androidtest.view.extensions.loadImage

class CountryListAdapter(private val rates: Rates) :
    RecyclerView.Adapter<CountryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_country_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rates.countryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country: Country = rates.countryList[position]
        holder.tvCountryCurrency.text = country.countryName
        holder.tvCountryFullCurrency.text = CountryInfo.valueOf(country.countryName).countryFullName
        holder.etRate.setText(country.rate.toString())
        holder.imgCountryFlag.loadImage(CountryInfo.valueOf(country.countryName).countryIcon)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCountryFlag: ImageView = itemView.findViewById(R.id.countryFlag)
        val tvCountryCurrency: TextView = itemView.findViewById(R.id.countryCurrency)
        val tvCountryFullCurrency: TextView = itemView.findViewById(R.id.countryFullCurrency)
        val etRate: EditText = itemView.findViewById(R.id.rate)
    }
}