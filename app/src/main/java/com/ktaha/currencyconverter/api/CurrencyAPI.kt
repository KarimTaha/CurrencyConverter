package com.ktaha.currencyconverter.api

import com.ktaha.currencyconverter.models.CurrencyResponse
import com.ktaha.currencyconverter.util.Constants.BASE_CURRENCY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("/latest")
    suspend fun getLatestCurrencies(
        @Query("base")
        baseCurrency: String = BASE_CURRENCY
    ): Response<CurrencyResponse>
}