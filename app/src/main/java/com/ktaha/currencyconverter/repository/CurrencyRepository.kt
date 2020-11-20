package com.ktaha.currencyconverter.repository

import com.ktaha.currencyconverter.api.RetrofitInstance

class CurrencyRepository {
    suspend fun getCurrencies() =
            RetrofitInstance.api.getLatestCurrencies()
}