package com.ktaha.currencyconverter.ui.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.ktaha.currencyconverter.R
import com.ktaha.currencyconverter.ui.CurrencyViewModel
import com.ktaha.currencyconverter.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_convert.*

class ConvertFragment : Fragment(R.layout.fragment_convert) {

    lateinit var viewModel: CurrencyViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        
        etSourceAmount.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode != KeyEvent.KEYCODE_BACK) {
                if (etSourceAmount.text.isNotEmpty()) {
                    val sourceAmount = etSourceAmount.text.toString().toDouble()
                    etTargetAmount.setText((viewModel.convertCurrency(sourceAmount, tvSourceCurrency.text.toString(), tvTargetCurrency.text.toString())).toString())
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
                    val targetAmount = etTargetAmount.text.toString().toDouble()
                    etSourceAmount.setText((viewModel.convertCurrency(targetAmount, tvTargetCurrency.text.toString(), tvSourceCurrency.text.toString())).toString())
                }
                else {
                    etSourceAmount.setText("")
                }
            }
            false
        }
    }
}