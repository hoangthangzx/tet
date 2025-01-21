package com.metalsensor.gold.detector.finder.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metalsensor.gold.detector.finder.model.ApiCoin
import com.metalsensor.gold.detector.finder.model.Coin
import com.metalsensor.gold.detector.finder.model.EventMessage
import com.metalsensor.gold.detector.finder.model.ResponseWrapper
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CoinViewModel : ViewModel() {
    private val _coinList = MutableLiveData<List<Coin>>()
    val coinList: LiveData<List<Coin>> get() = _coinList

    init {
        _coinList.value = Const.getCoinList()
    }

    fun fetchCoinsForCountries(countries: List<String>) {
        val latch = java.util.concurrent.CountDownLatch(countries.size)

        for (countryId in countries) {
            val apiCall = ApiClient.api.getCoinsByCountry(countryId, limit = 1000)
            Log.d("CoinViewModel", "API Call URL: ${apiCall.request().url()}")

            apiCall.enqueue(object : Callback<ResponseWrapper> {
                override fun onResponse(call: Call<ResponseWrapper>, response: Response<ResponseWrapper>) {
                    if (response.isSuccessful) {
                        val apiCoinList = response.body()?.list ?: emptyList()

                        val coinList = apiCoinList.filter { apiCoin ->
                            val obverseImage = apiCoin.image?.obverse
                            val reverseImage = apiCoin.image?.reverse
                            !(obverseImage?.contains("no-reverse-coin-en") == true ||
                                    reverseImage?.contains("no-reverse-coin-en") == true)
                        }.map { apiCoin ->
                            Coin(
                                countryId=countryId,
                                id = apiCoin.id,
                                name = apiCoin.name,
                                obverseImage = apiCoin.image?.obverse,
                                reverseImage = apiCoin.image?.reverse
                            )

                        }

                        Const.coinlist = coinList.size
                        Log.d("CoinViewModel", "Coins for $countryId: ${coinList.size}")
                        val updatedList = _coinList.value.orEmpty() + coinList
                        _coinList.postValue(updatedList)
                        Const.addCoins(coinList)
                    } else {
                        Log.e("CoinViewModel", "Error for $countryId: ${response.code()}, ${response.message()}")
                    }
                    latch.countDown()
                }

                override fun onFailure(call: Call<ResponseWrapper>, t: Throwable) {
                    Log.e("CoinViewModel", "API call failed for $countryId: ${t.message}")
                    latch.countDown()
                }
            })
        }
        Thread {
            latch.await()
            Log.d("CoinViewModel", "All API calls completed.")
        }.start()
    }


}