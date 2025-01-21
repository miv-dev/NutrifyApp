package ru.nutrify.data.models

import io.ktor.client.statement.*

sealed interface ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>
    data class UnhandledException(val exception: Throwable) : ApiResponse<Nothing>
    data class ApiException(val response: HttpResponse) : ApiResponse<Nothing>
}
