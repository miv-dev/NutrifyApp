package ru.nutrify.domain.entity

sealed interface AppError {

    data class FieldsError(val fields: Map<String, String>) : AppError
    data class ServerError(val message: String) : AppError
    data class ThisProgramAlreadyParsed(val path: String, val id: String) : AppError
    data object Network : AppError
    data object NotFound : AppError
    data object AccessDenied : AppError
    data object ServiceUnavailable : AppError
    data object Unknown : AppError
}