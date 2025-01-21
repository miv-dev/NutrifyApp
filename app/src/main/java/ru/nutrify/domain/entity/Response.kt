package ru.nutrify.domain.entity

sealed interface Response<out T> {
    data class Error(val error: AppError) : Response<Nothing>

    data class Success<T>(val data: T) : Response<T>
}
