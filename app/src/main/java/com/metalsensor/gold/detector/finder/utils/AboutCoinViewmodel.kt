package com.metalsensor.gold.detector.finder.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import com.metalsensor.gold.detector.finder.model.CoinDetails
import com.metalsensor.gold.detector.finder.model.EventMessage
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutCoinViewmodel : ViewModel() {

    fun fetchCoinDetails() {
        ApiClient.api.getCoinDetails(Const.id).enqueue(object : Callback<CoinDetails> {
            override fun onResponse(call: Call<CoinDetails>, response: Response<CoinDetails>) {
                try {
                    if (response.isSuccessful) {
                        val coinDetails = response.body()
                        coinDetails?.let {
                            // Bọc các logic gán giá trị trong try-catch
                            try {
                                Const.Metal = it.metal ?: "Unknown"
                                Const.Rarity = it.rarity_index ?: "Unknown"
                                Const.Shape = it.shape ?: "Unknown"
                                Const.Value = it.value ?: "Unknown"
                                Const.Weight = it.weight?.toString() ?: "Unknown"
                                Const.Years = it.years_range ?: "Unknown"

                                Log.d("CoinViewModel", """
                                    Title: ${it.title ?: "Unknown"}
                                    Metal: ${it.metal}
                                    Rarity Index: ${it.rarity_index}
                                    Shape: ${it.shape}
                                    Value: ${it.value}
                                    Weight: ${it.weight}
                                    Years Range: ${it.years_range}
                                """.trimIndent())

//                            playEffect(playingIndex, () -> {
//                                // nothing
//                            });
                                EventBus.getDefault().post(EventMessage("true"))
                            } catch (e: Exception) {
                                Log.e("CoinViewModel", "Error while parsing coin details: ${e.message}")
                            }
                        } ?: run {
                            Log.e("CoinViewModel", "Response body is null")
                        }
                    } else {
                        Log.e("CoinViewModel", "Error: ${response.code()}, ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("CoinViewModel", "Exception in onResponse: ${e.message}")
                }
            }

            override fun onFailure(call: Call<CoinDetails>, t: Throwable) {
                try {
                    Log.e("CoinViewModel", "API call failed: ${t.message}")
                } catch (e: Exception) {
                    Log.e("CoinViewModel", "Exception in onFailure: ${e.message}")
                }
            }
        })
    }
}
