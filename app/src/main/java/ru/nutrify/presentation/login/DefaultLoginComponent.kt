package ru.nutrify.presentation.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import ru.nutrify.presentation.ext.componentScope

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultLoginComponent @AssistedInject constructor(
    private val factory: LoginStoreFactory,
    @Assisted("onRegisterClicked") private val onRegisterClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : LoginComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { factory.create() }


    override val model: StateFlow<LoginStore.State> = store.stateFlow
    override fun changeUsername(username: String) {
        store.accept(LoginStore.Intent.ChangeUsername(username))
    }

    override fun changePassword(password: String) {
        store.accept(LoginStore.Intent.ChangePassword(password))
    }

    override fun onLoginClick() {
        store.accept(LoginStore.Intent.ClickLogin)
    }

    override fun onRegisterClick() {
        onRegisterClicked()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onRegisterClicked") onRegisterClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultLoginComponent
    }
}