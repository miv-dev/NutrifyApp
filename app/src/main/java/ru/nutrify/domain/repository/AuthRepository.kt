package ru.nutrify.domain.repository


import kotlinx.coroutines.flow.StateFlow
import ru.nutrify.domain.entity.AuthState
import ru.nutrify.domain.entity.Response
import ru.nutrify.domain.entity.User

interface AuthRepository {
    val authState: StateFlow<AuthState>

    suspend fun login(username: String, password: String): Response<Unit>

    suspend fun register(username: String, email: String, password: String): Response<Unit>

    fun logout()

    fun checkAuthState()

    fun getUser(): User?
}