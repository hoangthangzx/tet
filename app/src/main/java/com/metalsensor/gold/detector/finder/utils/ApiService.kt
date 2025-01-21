package com.metalsensor.gold.detector.finder.utils

import com.metalsensor.gold.detector.finder.model.CoinDetails
import com.metalsensor.gold.detector.finder.model.Lisaboutcoint
import com.metalsensor.gold.detector.finder.model.ResponseWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("coin/")
    fun getCoinDetails(@Query("coin_id") coinId: Int): Call<CoinDetails>
    @GET("country/coins/")
    fun getCoinsByCountry(
        @Query("country_id") countryId: String,
        @Query("limit") limit: Int = 1000
    ): Call<ResponseWrapper>

}
