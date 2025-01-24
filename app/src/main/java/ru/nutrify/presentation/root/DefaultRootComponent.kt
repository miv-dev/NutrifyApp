package ru.nutrify.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.nutrify.domain.entity.AuthState
import ru.nutrify.domain.usecases.auth.GetAuthStateUseCase
import ru.nutrify.presentation.auth.DefaultAuthComponent
import ru.nutrify.presentation.ext.componentScope
import ru.nutrify.presentation.home.DefaultHomeComponent
import ru.nutrify.presentation.root.RootComponent.Child

class DefaultRootComponent @AssistedInject constructor(
    private val authComponentFactory: DefaultAuthComponent.Factory,
    private val homeComponentFactory: DefaultHomeComponent.Factory,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    @Assisted("componentContext") componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()
    private val scope = componentScope()

    init {
        scope.launch {
            getAuthStateUseCase().collect{
                when(it){
                    is AuthState.Authorized -> navigation.replaceAll(Config.Home)
                    AuthState.Initial -> navigation.replaceAll(Config.Loading)
                    AuthState.NonAuthorized -> navigation.replaceAll(Config.Auth)
                }
            }
        }
    }


    override val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Loading,
        childFactory = ::child
    )


    private fun child(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            Config.Home -> Child.Home(homeComponentFactory.create(componentContext))
            Config.Auth -> Child.Auth(authComponentFactory.create(componentContext))
            Config.Loading -> Child.Loading
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Auth : Config

        @Serializable
        data object Loading : Config

        @Serializable
        data object Home : Config

    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}