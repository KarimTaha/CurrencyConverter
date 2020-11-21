package com.ktaha.currencyconverter.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ktaha.currencyconverter.R
import com.ktaha.currencyconverter.ui.CurrencyViewModel
import com.ktaha.currencyconverter.ui.MainActivity
import com.ktaha.currencyconverter.util.Resource
import kotlinx.android.synthetic.main.fragment_convert.*


class ConvertFragment : Fragment(R.layout.fragment_convert) {

    private lateinit var viewModel: CurrencyViewModel
    private var currenciesArray = arrayListOf<String>()
    private var setSourceCurrency = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        val arrayAdapter = ArrayAdapter(
                (activity as MainActivity).baseContext,
                android.R.layout.simple_spinner_item,
                currenciesArray
        )

        // setup the alert builder
        val builder = AlertDialog.Builder(context)
                .setTitle("Choose currency")
                .setAdapter(arrayAdapter) { dialogInterface, which ->
            if (setSourceCurrency) {
                btnSourceCurrency.text = arrayAdapter.getItem(which)
                if (etSourceAmount.text.isNotEmpty()) {
                    convertCurrency("source")
                }
            }
            else {
                btnTargetCurrency.text = arrayAdapter.getItem(which)
                if (etTargetAmount.text.isNotEmpty()) {
                    convertCurrency("target")
                }
            }
        }

        // create and show the alert dialog
        val dialog = builder.create()

        btnSourceCurrency.setOnClickListener {
            setSourceCurrency = true
            dialog.show()
        }
        btnTargetCurrency.setOnClickListener {
            setSourceCurrency = false
            dialog.show()
        }

        viewModel.currencies.observe(viewLifecycleOwner, { response->
            when(response) {
                is Resource.Success -> {
                    response.data?.let { currenciesResponse->
                        currenciesArray.addAll(currenciesResponse.rates.getCurrenciesArray())
                        arrayAdapter.notifyDataSetChanged()
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
        
        etSourceAmount.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode != KeyEvent.KEYCODE_BACK) {
                if (etSourceAmount.text.isNotEmpty()) {
                    convertCurrency("source")
                }
                else {
                    etTargetAmount.setText("")
                }
            }
            false
        }

        etTargetAmount.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode != KeyEvent.KEYCODE_BACK) {
                if (etTargetAmount.text.isNotEmpty()) {
                    convertCurrency("target")
                }
                else {
                    etSourceAmount.setText("")
                }
            }
            false
        }

        etSourceAmount.setOnFocusChangeListener { _, _ ->
            hideKeyboard()
        }
        etTargetAmount.setOnFocusChangeListener { _, _ ->
            hideKeyboard()
        }
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun convertCurrency(from: String) {
        when (from) {
            "source" -> {
                val sourceAmount = etSourceAmount.text.toString().toDouble()
                etTargetAmount.setText((viewModel.convertCurrency(sourceAmount, btnSourceCurrency.text.toString(), btnTargetCurrency.text.toString())).toString())
            }
            "target" -> {
                val targetAmount = etTargetAmount.text.toString().toDouble()
                etSourceAmount.setText((viewModel.convertCurrency(targetAmount, btnTargetCurrency.text.toString(), btnSourceCurrency.text.toString())).toString())
            }
        }
    }
}