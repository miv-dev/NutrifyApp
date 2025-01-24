package ru.nutrify.presentation.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import ru.nutrify.presentation.home.HomeComponent.Child

class DefaultHomeComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
) : HomeComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()


    override val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Main,
        childFactory = ::child
    )

    override fun navigateToMain() {
        navigation.bringToFront(Config.Main)
    }

    override fun navigateToAccount() {
        navigation.bringToFront(Config.Account)
    }

    override fun navigateToSearch() {
        navigation.bringToFront(Config.Search)
    }


    private fun child(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            Config.Account -> Child.Account
            Config.Main -> Child.Main
            Config.Search -> Child.Search
        }

    @Serializable
    private sealed interface Config  {
        @Serializable
        data object Main : Config

        @Serializable
        data object Search : Config

        @Serializable
        data object Account : Config

    }


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultHomeComponent
    }
}