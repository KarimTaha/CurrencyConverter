package com.ktaha.currencyconverter.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ktaha.currencyconverter.R
import com.ktaha.currencyconverter.adapters.CurrenciesAdapter
import com.ktaha.currencyconverter.models.Currency
import com.ktaha.currencyconverter.models.Rates
import com.ktaha.currencyconverter.ui.CurrencyViewModel
import com.ktaha.currencyconverter.ui.MainActivity
import com.ktaha.currencyconverter.util.Resource
import kotlinx.android.synthetic.main.fragment_list_currencies.*

class ListCurrenciesFragment : Fragment(R.layout.fragment_list_currencies){

    lateinit var viewModel: CurrencyViewModel
    lateinit var currenciesAdapter: CurrenciesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.currencies.observe(viewLifecycleOwner, Observer { response->
            when(response) {
                is Resource.Success -> {
                    response.data?.let { currenciesResponse->
                        currenciesAdapter.submitListToDiffer(currenciesResponse.rates)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {

                }
            }
        })
    }

    private fun setupRecyclerView() {
        currenciesAdapter = CurrenciesAdapter()
        rvCurrencies.apply {
            adapter = currenciesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}







