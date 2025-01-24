package ru.nutrify.presentation.login

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.nutrify.domain.entity.Response
import ru.nutrify.domain.usecases.auth.LoginUseCase
import ru.nutrify.presentation.ext.errorToString
import javax.inject.Inject

interface LoginStore : Store<LoginStore.Intent, LoginStore.State, LoginStore.Label> {

    sealed interface Intent {
        data class ChangeUsername(val username: String) : Intent
        data class ChangePassword(val password: String) : Intent
        data object ClickLogin : Intent
    }

    data class State(
        val username: String,
        val usernameError: String?,
        val password: String,
        val passwordError: String?,

        val loginState: LoginState
    ) {
        sealed interface LoginState {
            data object Initial : LoginState
            data object Loading : LoginState
            data class Error(val error: String) : LoginState
            data object SuccessLogin : LoginState
        }
    }

    sealed class Label {

    }
}

class LoginStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val loginUseCase: LoginUseCase,
) {

    fun create(): LoginStore =
        object : LoginStore,
            Store<LoginStore.Intent, LoginStore.State, LoginStore.Label> by storeFactory.create(
                name = "LoginStore",
                initialState = LoginStore.State(
                    username = "",
                    password = "",
                    loginState = LoginStore.State.LoginState.Initial,
                    passwordError = null,
                    usernameError = null
                ),
                bootstrapper = BootstrapperImpl(),
                executorFactory = { ExecutorImpl() },
                reducer = ReducerImpl
            ) {}

    private sealed class Action {
    }

    private sealed interface Msg {
        data class ChangeEmail(val email: String) : Msg
        data class ChangePassword(val password: String) : Msg
        data class ChangePasswordError(val error: String?) : Msg
        data class ChangeEmailError(val error: String?) : Msg
        data class LoginResultError(val error: String) : Msg
        data object LoginResultLoading : Msg
        data object LoginResultSuccess : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<LoginStore.Intent, Action, LoginStore.State, Msg, LoginStore.Label>() {

        override fun executeIntent(intent: LoginStore.Intent) {
            val state = state()

            when (intent) {

                is LoginStore.Intent.ChangeUsername -> {
                    if (!state.usernameError.isNullOrEmpty()) {
                        dispatch(Msg.ChangeEmailError(null))
                    }

                    dispatch(Msg.ChangeEmail(intent.username))
                }

                is LoginStore.Intent.ChangePassword -> {
                    if (!state.passwordError.isNullOrEmpty()) {
                        dispatch(Msg.ChangePasswordError(null))
                    }

                    dispatch(Msg.ChangePassword(intent.password))

                }

                LoginStore.Intent.ClickLogin -> {
                    if (state.username.isNotEmpty() && state.password.isNotEmpty()) {
                        dispatch(Msg.LoginResultLoading)
                        scope.launch {
                            when (val result = loginUseCase(state.username, state.password)) {
                                is Response.Success<*> -> {
                                    dispatch(Msg.LoginResultSuccess)
                                }

                                is Response.Error -> {
                                    dispatch(Msg.LoginResultError(errorToString(result.error)))
                                }
                            }
                        }
                    } else {
                        if (state.username.isEmpty()) {
                            dispatch(Msg.ChangeEmailError("Недолжно быть пустым"))
                        }
                        if (state.password.isEmpty()) {
                            dispatch(Msg.ChangePasswordError("Недолжно быть пустым"))
                        }
                    }

                }
            }
        }
    }

    private object ReducerImpl : Reducer<LoginStore.State, Msg> {
        override fun LoginStore.State.reduce(msg: Msg): LoginStore.State = when (msg) {
            is Msg.ChangeEmail -> copy(username = msg.email)
            is Msg.ChangePassword -> copy(password = msg.password)
            is Msg.LoginResultError -> copy(loginState = LoginStore.State.LoginState.Error(msg.error))
            Msg.LoginResultLoading -> copy(loginState = LoginStore.State.LoginState.Loading)
            Msg.LoginResultSuccess -> copy(loginState = LoginStore.State.LoginState.SuccessLogin)
            is Msg.ChangeEmailError -> copy(usernameError = msg.error)
            is Msg.ChangePasswordError -> copy(passwordError = msg.error)
        }
    }
}