package ru.nutrify.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ru.nutrify.data.dto.TokenDTO
import ru.nutrify.data.dto.UserDTO
import ru.nutrify.data.models.ApiResponse
import ru.nutrify.data.utils.result
import javax.inject.Inject

class UserApiService @Inject constructor(
    private val client: HttpClient
) {
    suspend fun login(username: String, password: String): ApiResponse<TokenDTO> {
        return try {
            client.post(ApiEndpoints.LOGIN) {
                setBody(
                    hashMapOf("username" to username, "password" to password)
                )
            }.result()
        } catch (e: Exception) {
            ApiResponse.UnhandledException(e)
        }
    }

    suspend fun register(email: String, username: String, password: String): ApiResponse<TokenDTO> {
        return try {
            client.post(ApiEndpoints.REGISTER) {
                setBody(
                    hashMapOf(
                        "email" to email,
                        "password" to password,
                        "username" to username
                    )
                )
            }.result()
        } catch (e: Exception) {
            ApiResponse.UnhandledException(e)
        }
    }

    suspend fun getCurrentUser(): ApiResponse<UserDTO>{
        return try {
            client.get(ApiEndpoints.CURRENT_USER).result()
        } catch (e: Exception) {
            ApiResponse.UnhandledException(e)
        }
    }

    fun clearTokens() {
        client.authProvider<BearerAuthProvider>()?.clearToken()
    }
}