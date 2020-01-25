package com.revolut.androidtest.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class CurrencyListLiveData<T> : MutableLiveData<ListHolder<T>>() {
    val size: Int
        get() {
            return value?.size() ?: -1
        }


    fun addItem(item: T, position: Int = value?.size() ?: 0) {
        //value?.addItem(position, item)
        value = ListHolder(mutableListOf(item))
    }

    fun setItem(position: Int, item: T) {
        value?.setItem(position, item)
        value = value
    }

    operator fun get(position: Int): T? {
        return value?.list?.get(position)
    }
}

data class ListHolder<T>(val list: MutableList<T> = mutableListOf()) {
    var indexChanged: Int = -1
    private var updateType: UpdateType? = null

    fun addItem(position: Int, item: T) {
        list.add(position, item)
        indexChanged = position
        updateType = UpdateType.INSERT
    }

    fun setItem(position: Int, item: T) {
        list[position] = item
        indexChanged = position
        updateType = UpdateType.CHANGE
    }

    fun size(): Int {
        return list.size
    }

    fun applyChange(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        updateType?.notifyChange(adapter, indexChanged)
    }

    private enum class UpdateType {
        INSERT {
            override fun notifyChange(
                adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
                indexChanged: Int) = adapter.notifyItemInserted(indexChanged)
        },
        CHANGE {
            override fun notifyChange(
                adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
                indexChanged: Int
            ) = adapter.notifyItemChanged(indexChanged)
        };

        abstract fun notifyChange(
            adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
            indexChanged: Int
        )
    }
}