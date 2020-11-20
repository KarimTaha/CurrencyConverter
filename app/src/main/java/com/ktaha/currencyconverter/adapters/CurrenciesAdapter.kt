package com.ktaha.currencyconverter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ktaha.currencyconverter.R
import com.ktaha.currencyconverter.models.Currency
import com.ktaha.currencyconverter.models.Rates
import kotlinx.android.synthetic.main.item_currency.view.*

class CurrenciesAdapter: RecyclerView.Adapter<CurrenciesAdapter.CurrencyViewHolder>() {

    inner class CurrencyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object: DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.currency == newItem.currency
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    fun submitListToDiffer(rates: Rates) {
        differ.submitList(getCurrenciesList(rates))
    }

    private fun getCurrenciesList(rates:Rates):List<Currency> {
        val currenciesMap = rates.getCurrenciesMap()
        var resultList = mutableListOf<Currency>()
        for (currencyCode in currenciesMap) {
            resultList.add(Currency(currencyCode.key, currencyCode.value))
        }

        return resultList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_currency,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = differ.currentList[position]
        holder.itemView.apply {
            tvCurrencyName.text = currency.currency
            tvCurrencyRate.text = currency.rate.toString()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}