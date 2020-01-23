package com.revolut.androidtest.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.revolut.androidtest.R
import com.revolut.androidtest.domain.model.Country
import com.revolut.androidtest.view.extensions.loadImage

class CountryListAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<CountryListAdapter.ViewHolder>() {
    private var rates: MutableList<Country> = mutableListOf()
    private var base = String()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_country_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rates.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country: Country = rates[position]
        holder.tvCountryCurrency.text = country.countryCurrency
        holder.tvCountryFullCurrency.text = CountryInfo.valueOf(country.countryCurrency).countryFullName
        holder.etRate.setText(country.rate.toString())
        holder.imgCountryFlag.loadImage(CountryInfo.valueOf(country.countryCurrency).countryIcon)
        holder.itemView.setOnClickListener {
            country.let { listener.invoke(position) }
        }
    }

    fun setItems(newItems: List<Country>) {
        rates.clear()
        rates.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setBase(base: String) {
        this.base = base
    }

    fun moveItem(clickedPos: Int, toPosition: Int) {
        if (clickedPos == toPosition) return

        val movingItem: Country = this.rates.removeAt(clickedPos)
        if (clickedPos < toPosition) {
            this.rates.add(toPosition - 1, movingItem)
        } else {
            this.rates.add(toPosition, movingItem)
        }

        notifyItemMoved(clickedPos, toPosition)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCountryFlag: ImageView = itemView.findViewById(R.id.countryFlag)
        val tvCountryCurrency: TextView = itemView.findViewById(R.id.countryCurrency)
        val tvCountryFullCurrency: TextView = itemView.findViewById(R.id.countryFullCurrency)
        val etRate: EditText = itemView.findViewById(R.id.rate)
    }
}