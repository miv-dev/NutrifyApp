package ru.nutrify.presentation.ext

import ru.nutrify.domain.entity.AppError

fun errorToString(error: AppError): String {
    return when (error) {
        AppError.AccessDenied -> "Credentials isn't correct"
        is AppError.FieldsError -> "Fields format error"
        AppError.NotFound, AppError.Network -> "Network Error"

        AppError.ServiceUnavailable -> "Server is not respond"
        is AppError.ServerError -> error.message
        AppError.Unknown -> "Unknown Error"
        is AppError.ThisProgramAlreadyParsed -> "Program (${error.id}) already parsed"
    }
}