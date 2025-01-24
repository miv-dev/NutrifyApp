package ru.nutrify.presentation.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import ru.nutrify.presentation.login.DefaultLoginComponent
import ru.nutrify.presentation.register.DefaultRegisterComponent

class DefaultAuthComponent @AssistedInject constructor(
    private val loginComponentFactory: DefaultLoginComponent.Factory,
    private val registerComponentFactory: DefaultRegisterComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext,
) : AuthComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()


    override val stack: Value<ChildStack<*, AuthComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Login,
        childFactory = ::child
    )

    override fun navigateToLogin() {
        navigation.replaceCurrent(Config.Login)
    }

    override fun navigateToRegister() {
        navigation.replaceCurrent(Config.Register)
    }


    private fun child(config: Config, componentContext: ComponentContext): AuthComponent.Child =
        when (config) {
            Config.Login -> {
                AuthComponent.Child.Login(
                    loginComponentFactory.create(
                        { navigateToRegister() },
                        componentContext
                    )
                )
            }

            Config.Register -> {
                AuthComponent.Child.Register(registerComponentFactory.create(componentContext))
            }
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Login : Config

        @Serializable
        data object Register : Config
    }


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultAuthComponent
    }
}