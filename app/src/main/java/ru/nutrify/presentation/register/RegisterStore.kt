package ru.nutrify.presentation.register

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import ru.nutrify.presentation.register.RegisterStore.Intent
import ru.nutrify.presentation.register.RegisterStore.Label
import ru.nutrify.presentation.register.RegisterStore.State
import javax.inject.Inject

interface RegisterStore : Store<Intent, State, Label> {

    sealed interface Intent {
        class ChangeUsername(val username: String) : Intent
        class ChangeEmail(val email: String) : Intent
        class ChangePassword(val password: String) : Intent


    }

    data class State(
        val username: String = "",
        val usernameError: String? = null,
        val email: String = "",
        val emailError: String? = null,
        val password: String = "",
        val passwordError: String? = null,
    )

    sealed interface Label {
    }
}

class RegisterStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(): RegisterStore =
        object : RegisterStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegisterStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        class UsernameChanged(val username: String) : Msg
        class EmailChanged(val email: String) : Msg
        class PasswordChanged(val password: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChangeUsername -> dispatch(Msg.UsernameChanged(intent.username))
                is Intent.ChangeEmail -> dispatch(Msg.EmailChanged(intent.email))
                is Intent.ChangePassword -> dispatch(Msg.PasswordChanged(intent.password))
            }
        }

        override fun executeAction(action: Action) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.EmailChanged -> copy(email = msg.email)
                is Msg.PasswordChanged -> copy(password = msg.password)
                is Msg.UsernameChanged -> copy(username = msg.username)
            }
    }
}
