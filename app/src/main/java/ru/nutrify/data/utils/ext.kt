package ru.nutrify.data.utils

import io.ktor.client.call.*
import io.ktor.client.statement.*
import ru.nutrify.data.models.ApiResponse

fun HttpResponse.isSuccessful(): Boolean {
    return status.value in 200..299
}

suspend inline fun <reified T> HttpResponse.result(): ApiResponse<T> {
    return if (isSuccessful()) {
        ApiResponse.Success(body())
    } else {
        ApiResponse.ApiException(this)
    }
}