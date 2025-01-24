package ru.nutrify.domain.usecases.auth

import ru.nutrify.domain.repository.AuthRepository
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.authState
}