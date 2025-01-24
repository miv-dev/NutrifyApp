package ru.nutrify.presentation.register

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultRegisterComponent @AssistedInject  constructor(
    private val factory: RegisterStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RegisterComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { factory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<RegisterStore.State> = store.stateFlow

    override fun changeEmail(email: String) {
        store.accept(RegisterStore.Intent.ChangeEmail(email))
    }

    override fun changePassword(password: String) {
        store.accept(RegisterStore.Intent.ChangePassword(password))
    }

    override fun changeUsername(username: String) {
        store.accept(RegisterStore.Intent.ChangeUsername(username))
    }


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRegisterComponent
    }
}