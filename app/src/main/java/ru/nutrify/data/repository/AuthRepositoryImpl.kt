package ru.nutrify.data.repository

import ru.nutrify.data.utils.ErrorService
import ru.nutrify.domain.entity.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.nutrify.data.local.TokenService
import ru.nutrify.data.mapper.UserMapper
import ru.nutrify.data.models.ApiResponse
import ru.nutrify.data.remote.UserApiService
import ru.nutrify.domain.entity.Response
import ru.nutrify.domain.entity.User
import ru.nutrify.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: UserApiService,
    private val tokenService: TokenService,
    private val errorService: ErrorService
) : AuthRepository {

    private val scope = CoroutineScope(IO)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)


    override val authState: StateFlow<AuthState> = _authState

    override suspend fun login(email: String, password: String): Response<Unit> {
        return when (val response = apiService.login(email, password)) {

            is ApiResponse.Success -> {
                tokenService.setAccessToken(response.data.access)
                tokenService.setRefreshToken(response.data.refresh)
                checkAuthState()
                Response.Success(Unit)
            }

            is ApiResponse.ApiException -> {
                _authState.emit(AuthState.NonAuthorized)
                Response.Error(errorService.getError(response.response))
            }

            is ApiResponse.UnhandledException -> {
                _authState.emit(AuthState.NonAuthorized)
                Response.Error(errorService.getError(response.exception))
            }
        }


    }

    init {
        checkAuthState()
    }

    override fun logout() {
        scope.launch {
            apiService.clearTokens()
            _authState.emit(AuthState.NonAuthorized)
            tokenService.setAccessToken("")
            tokenService.setRefreshToken("")
        }
    }


    override fun checkAuthState() {

        scope.launch {
            when (val state = apiService.getCurrentUser()) {
                is ApiResponse.Success -> {
                    UserMapper.mapToUser(state.data).also {
                        _authState.emit(AuthState.Authorized(it))
                    }
                }

                is ApiResponse.ApiException,
                is ApiResponse.UnhandledException -> _authState.emit(AuthState.NonAuthorized)
            }
        }
    }

    override fun getUser(): User? {
        return when (val state = _authState.value) {
            is AuthState.Authorized -> state.user
            AuthState.Initial,
            AuthState.NonAuthorized -> null
        }
    }

}