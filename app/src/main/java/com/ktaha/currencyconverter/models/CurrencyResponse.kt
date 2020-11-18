package com.ktaha.currencyconverter.models

data class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: Rates
)