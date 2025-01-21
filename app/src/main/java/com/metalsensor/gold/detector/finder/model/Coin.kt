package com.metalsensor.gold.detector.finder.model

data class Coin(
    val countryId:String?,
    val id: Int,
    val name: String?,
    val obverseImage: String?, // URL for the obverse image
    val reverseImage: String?  // URL for the reverse image
)
