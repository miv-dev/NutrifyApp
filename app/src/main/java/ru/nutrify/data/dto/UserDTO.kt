package ru.nutrify.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val username: String,
    val email: String,
    val age: Int?,
    val gender: String,
    @SerialName("height_cm")
    val heightCm: Int?,
    @SerialName("weight_kg")
    val weightKg: Float?,
    @SerialName("daily_calorie_goal")
    val dailyCalorieGoal: Int?,
    @SerialName("protein_goal")
    val proteinGoal: Float?,
    @SerialName("fat_goal")
    val fatGoal: Float?,
    @SerialName("carb_goal")
    val carbGoal: Float?,
    @SerialName("activity_level")
    val activityLevel: String,
    @SerialName("dietary_preference")
    val dietaryPreference: String,
    val allergies: String?,
    @SerialName("profile_picture")
    val profilePicture: String?,
)