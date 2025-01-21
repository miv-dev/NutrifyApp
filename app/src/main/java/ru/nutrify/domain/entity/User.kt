package ru.nutrify.domain.entity

data class User(
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
    val activityLevel: ActivityLevel,
    val dietaryPreference: DietaryPreference,
    val allergies: String?,
    val profilePicture: String?,
)

enum class ActivityLevel {
    SEDENTARY,
    LIGHT,
    MODERATE,
    ACTIVE,
    VERY_ACTIVE;

}

enum class DietaryPreference {
    NONE,
    VEGETARIAN,
    VEGAN,
    KETO,
    PALEO,
    OTHER;
}