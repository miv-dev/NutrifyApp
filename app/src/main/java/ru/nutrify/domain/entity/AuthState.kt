package ru.nutrify.domain.entity

sealed class AuthState {
    data object Initial : AuthState()
    data object NonAuthorized : AuthState()
    data class Authorized(val user: User) : AuthState()
}