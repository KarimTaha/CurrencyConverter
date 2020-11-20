package com.ktaha.currencyconverter.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ktaha.currencyconverter.models.Currency
import com.ktaha.currencyconverter.models.CurrencyResponse
import com.ktaha.currencyconverter.repository.CurrencyRepository
import com.ktaha.currencyconverter.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class CurrencyViewModel(
        app: Application,
        private val currencyRepository: CurrencyRepository
): AndroidViewModel(app) {
    var currenciesArray = arrayListOf<Currency>()
    val currencies: MutableLiveData<Resource<CurrencyResponse>> = MutableLiveData()

    init {
        getCurrencies()
    }

    fun getCurrencies() = viewModelScope.launch {
        val response = currencyRepository.getCurrencies()
        currencies.postValue(handleCurrencyResponse(response))
    }

    private fun handleCurrencyResponse(response: Response<CurrencyResponse>): Resource<CurrencyResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}