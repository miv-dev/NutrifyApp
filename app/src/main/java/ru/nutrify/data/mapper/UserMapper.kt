package ru.nutrify.data.mapper

import ru.nutrify.data.dto.UserDTO
import ru.nutrify.domain.entity.ActivityLevel
import ru.nutrify.domain.entity.DietaryPreference
import ru.nutrify.domain.entity.User

object UserMapper {

    fun mapToUser(userDTO: UserDTO): User {
        return User(
            id = userDTO.id,
            username = userDTO.username,
            email = userDTO.email,
            age = userDTO.age,
            gender = userDTO.gender,
            heightCm = userDTO.heightCm,
            weightKg = userDTO.weightKg,
            dailyCalorieGoal = userDTO.dailyCalorieGoal,
            proteinGoal = userDTO.proteinGoal,
            fatGoal = userDTO.fatGoal,
            carbGoal = userDTO.carbGoal,
            activityLevel = ActivityLevel.valueOf(userDTO.activityLevel.uppercase()),
            dietaryPreference = DietaryPreference.valueOf(userDTO.dietaryPreference.uppercase()),
            allergies = userDTO.allergies,
            profilePicture = userDTO.profilePicture
        )
    }
}