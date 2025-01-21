package ru.nutrify.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(
    val access: String,
    val refresh: String
)
