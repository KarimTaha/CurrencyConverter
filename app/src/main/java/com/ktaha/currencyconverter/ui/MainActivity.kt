package com.ktaha.currencyconverter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ktaha.currencyconverter.R
import com.ktaha.currencyconverter.repository.CurrencyRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: CurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currencyRepository = CurrencyRepository()
        val currencyViewModelProviderFactory = CurrencyViewModelProviderFactory(application, currencyRepository)
        viewModel = ViewModelProvider(this, currencyViewModelProviderFactory).get(CurrencyViewModel::class.java)
        bottomNavigationView.setupWithNavController(currencyNavHostFragment.findNavController())
    }
}