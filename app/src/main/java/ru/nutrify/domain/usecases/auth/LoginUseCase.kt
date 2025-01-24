package ru.nutrify.domain.usecases.auth

import ru.nutrify.domain.repository.AuthRepository
import ru.nutrify.presentation.login.LoginStore
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String) =
        repository.login(username, password)
}