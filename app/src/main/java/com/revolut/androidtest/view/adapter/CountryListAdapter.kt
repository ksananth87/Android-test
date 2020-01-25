package com.revolut.androidtest.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.revolut.androidtest.R
import com.revolut.androidtest.domain.model.Currency
import com.revolut.androidtest.view.extensions.loadImage
import java.util.*

class CountryListAdapter(
    private val clickListener: (Int) -> Unit,
    private val textChangeListener: (Int, String, String) -> Unit
) :
    RecyclerView.Adapter<CountryListAdapter.ViewHolder>() {
    private var rates: MutableList<Currency> = mutableListOf()
    private lateinit var mRecyclerView: RecyclerView

    private val diffCallback = object : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.rate == newItem.rate
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.rate == newItem.rate
        }
    }
    private val mDiffer = AsyncListDiffer(this, this.diffCallback)

    fun submitList(list: List<Currency>) {
        //mDiffer.submitList(list)
        val diffResult = DiffUtil.calculateDiff(
            CurrencyDiffCallback(
                this.rates,
                list
            )
        )
        diffResult.dispatchUpdatesTo(this)
    }
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
        holder.etRate.setText(country.rate.toString())
        holder.imgCountryFlag.loadImage(CountryInfo.valueOf(country.code).countryIcon)
        holder.itemView.setOnClickListener {
            country.let {
                moveItem(position, 0)
                mRecyclerView.scrollToPosition(0)
            }
        }
        holder.etRate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(edittext: Editable?) {
                //mDiffer.currentList[position].rate = edittext.toString().toFloat()
                //country.let { textChangeListener.invoke(position, country.code, edittext.toString()) }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    fun setItems(newItems: List<Currency>) {
        rates.clear()
        rates.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateList(newList: ArrayList<Currency>) {
        val diffResult = DiffUtil.calculateDiff(CurrencyDiffCallback(this.rates, newList))
        diffResult.dispatchUpdatesTo(this)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        if (fromPosition == toPosition) return
        val fromItem = rates[fromPosition]
        val toItem = rates[toPosition]
        Collections.swap(
            rates,
            fromPosition,
            toPosition
        )
        notifyItemMoved(fromPosition, toPosition)
        notifyItemRangeChanged(1, rates.size)
        //notifyItemChanged(fromPosition, toItem)
        //notifyItemChanged(toPosition, toItem)
        Log.e("", "")
    }

    fun getItems(): MutableList<Currency> {
        return mDiffer.currentList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCountryFlag: ImageView = itemView.findViewById(R.id.countryFlag)
        val tvCountryCurrency: TextView = itemView.findViewById(R.id.countryCurrency)
        val tvCountryFullCurrency: TextView = itemView.findViewById(R.id.countryFullCurrency)
        val etRate: EditText = itemView.findViewById(R.id.rate)
    }
}