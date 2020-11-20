package com.ktaha.currencyconverter.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ktaha.currencyconverter.models.CurrencyResponse
import com.ktaha.currencyconverter.repository.CurrencyRepository
import com.ktaha.currencyconverter.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class CurrencyViewModel(
        app: Application,
        private val currencyRepository: CurrencyRepository
): AndroidViewModel(app) {
    val currencies: MutableLiveData<Resource<CurrencyResponse>> = MutableLiveData()
    val TAG = "CurrencyViewModel"

    init {
        getCurrencies()
    }

    private fun getCurrencies() = viewModelScope.launch {
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

    fun convertCurrency(amount: Double, sourceCurrency: String, targetCurrency: String):Double {
        Log.d(TAG, "Converting amount = $amount from $sourceCurrency to $targetCurrency")
        if (sourceCurrency == targetCurrency) {
            return amount
        }
        else {
            val response = currencies.value
            if (response is Resource.Success) {
                response.data?.let { currenciesResponse->
                    val ratesMap = currenciesResponse.rates.getCurrenciesMap()
                    if (!ratesMap.containsKey(sourceCurrency) || !ratesMap.containsKey(targetCurrency)) {
                        return -1.0
                    } else {
                        return when {
                            sourceCurrency == "USD" -> {
                                amount * (ratesMap[targetCurrency] ?: 1.0)
                            }
                            targetCurrency == "USD" -> {
                                amount / (ratesMap[sourceCurrency] ?: 1.0)
                            }
                            else -> {
                                val valueInUSD = amount / (ratesMap[sourceCurrency] ?: 1.0)
                                valueInUSD * (ratesMap[targetCurrency] ?: 1.0)
                            }
                        }
                    }
                }
            }
        }
        return -1.0
    }
}