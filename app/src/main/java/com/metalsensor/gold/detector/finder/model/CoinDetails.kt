package com.metalsensor.gold.detector.finder.model

data class CoinDetails(
    val commemorative_name: String,
    val country: Country,
    val id: Int,
    val metal: String,
    val rarity_index: String,
    val shape: String,
    val title: String,
    val value: String,
    val weight: Double,
    val years_range: String
)