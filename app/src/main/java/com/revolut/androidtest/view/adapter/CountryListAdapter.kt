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
import java.util.*

class CountryListAdapter(
    private val textChangeListener: (String,Float, Float) -> Unit
) :
    RecyclerView.Adapter<CountryListAdapter.ViewHolder>() {
    private var rates: MutableList<Currency> = mutableListOf()
    private lateinit var mRecyclerView: RecyclerView
    private var editedCode: String = ""
    private var editedAmount: Float = 0f

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }
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
        holder.etRate.setText(editedCode, country.code, country.rate.toString())
        holder.imgCountryFlag.loadImage(CountryInfo.valueOf(country.code).countryIcon)
        holder.itemView.setOnClickListener {
            country.let {
                moveTappedCurrencyToTop(position)
                mRecyclerView.scrollToPosition(TOP_POSITION)
            }
        }
        holder.etRate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edittext: Editable?) {
                if (holder.etRate.isFocused) {
                    updateEditedCode(country)
                    updateEditedAmount(edittext)
                    country.let { textChangeListener.invoke(country.code, country.rate, editedAmount) }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    private fun updateEditedAmount(edittext: Editable?) {
        editedAmount = if (edittext.toString().isEmpty()) 0f
        else
            edittext.toString().toFloat()
    }

    private fun updateEditedCode(country: Currency) {
        editedCode = country.code
    }

    fun setCurrencyList(newItems: List<Currency>) {
        rates.clear()
        rates.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateCurrencyList(newList: ArrayList<Currency>) {
        newList.forEachIndexed { index, element ->
            notifyItemChanged(index, element)
        }
        rates = newList
    }

    private fun moveTappedCurrencyToTop(tappedPosition: Int) {
        if (tappedPosition == TOP_POSITION) return
        val tappedCurrency = rates[tappedPosition]
        rates.removeAt(tappedPosition)
        rates.add(TOP_POSITION, tappedCurrency)
        notifyItemMoved(tappedPosition, TOP_POSITION)
    }

    fun getItems(): MutableList<Currency> {
        return rates
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCountryFlag: ImageView = itemView.findViewById(R.id.countryFlag)
        val tvCountryCurrency: TextView = itemView.findViewById(R.id.countryCurrency)
        val tvCountryFullCurrency: TextView = itemView.findViewById(R.id.countryFullCurrency)
        val etRate: EditText = itemView.findViewById(R.id.rate)
    }

    companion object {
        const val TOP_POSITION = 0
    }
}

private fun EditText.setText(editedCode: String, code: String, rate: String) {
    if (editedCode == "" || editedCode != code) {
        if (rate == "0.0") {
            text = null
        } else {
            setText(rate)
        }
    }
}
