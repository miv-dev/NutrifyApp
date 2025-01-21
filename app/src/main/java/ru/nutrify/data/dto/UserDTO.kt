package ru.nutrify.data.dto

data class UserDTO(
    val id: Int,
    val username: String,
    val email: String,
    val age: Int?,
    val gender: String,
    val heightCm: Int?,
    val weightKg: Float?,
    val dailyCalorieGoal: Int?,
    val proteinGoal: Float?,
    val fatGoal: Float?,
    val carbGoal: Float?,
    val activityLevel: String,
    val dietaryPreference: String,
    val allergies: String?,
    val profilePicture: String?,
)