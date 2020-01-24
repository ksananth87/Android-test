package com.revolut.androidtest.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.revolut.androidtest.R
import com.revolut.androidtest.domain.model.Currency
import com.revolut.androidtest.view.extensions.loadImage

class CountryListAdapter(
    private val clickListener: (Int) -> Unit,
    private val textChangeListener: (Int, String, String) -> Unit
) :
    RecyclerView.Adapter<CountryListAdapter.ViewHolder>() {
    private var rates: MutableList<Currency> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_country_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rates.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country: Currency = rates[position]
        holder.tvCountryCurrency.text = country.code
        holder.tvCountryFullCurrency.text = CountryInfo.valueOf(country.code).countryFullName
        holder.etRate.setText(country.rate.toString())
        holder.imgCountryFlag.loadImage(CountryInfo.valueOf(country.code).countryIcon)
        holder.itemView.setOnClickListener {
            country.let { clickListener.invoke(position) }
        }
        holder.etRate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                country.let { textChangeListener.invoke(position, country.code, text.toString()) }
            }

        })
    }

    fun setItems(newItems: List<Currency>) {
        rates.clear()
        rates.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateItems(newItems: List<Currency>) {
        rates.clear()
        rates.addAll(newItems)
    }

    fun moveItem(clickedPos: Int, toPosition: Int) {
        if (clickedPos == toPosition) return
        notifyItemMoved(clickedPos, toPosition)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCountryFlag: ImageView = itemView.findViewById(R.id.countryFlag)
        val tvCountryCurrency: TextView = itemView.findViewById(R.id.countryCurrency)
        val tvCountryFullCurrency: TextView = itemView.findViewById(R.id.countryFullCurrency)
        val etRate: EditText = itemView.findViewById(R.id.rate)
    }
}