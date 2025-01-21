package ru.nutrify.data.utils

import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable
import ru.nutrify.domain.entity.AppError
import javax.inject.Inject

class ErrorService @Inject constructor() {

    fun getError(exception: Throwable): AppError {
        return when (exception) {
            is java.net.UnknownHostException -> AppError.Network
            is java.net.SocketTimeoutException -> AppError.Network
            is java.net.ConnectException -> AppError.Network
            is java.net.ProtocolException -> AppError.Network
            is java.net.MalformedURLException -> AppError.Network
            is java.net.URISyntaxException -> AppError.Network
            else -> {
                AppError.Unknown
            }
        }
    }

    suspend fun getError(httpResponse: HttpResponse): AppError {
        return try {
            val body = httpResponse.body<GeneralError>()
            AppError.ServerError(body.message)
        } catch (e: Exception) {
            return try {
                httpResponse.body<String>().let {
                    AppError.FieldsError(parseFieldsError(it))
                }
            } catch (e: Exception) {
                AppError.Unknown
            }
        }
    }

}

@Serializable
data class GeneralError(val message: String)

fun parseFieldsError(error: String): Map<String, String> {
    val fields = error.substringAfter("{").substringBefore("}").split(",")
    val map = mutableMapOf<String, String>()
    fields.forEach {
        val key = it.substringBefore(":").replace("\"", "").trim()
        val value = it.substringAfter(":").replace("\"", "").trim()

        map[key] = value.replace("[", "").replace("]", "")
    }
    return map
}