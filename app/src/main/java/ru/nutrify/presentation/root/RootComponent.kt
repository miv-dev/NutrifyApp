package ru.nutrify.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.nutrify.presentation.auth.AuthComponent
import ru.nutrify.presentation.home.HomeComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Home(val component: HomeComponent): Child
        data class Auth(val component: AuthComponent): Child
        data object Loading: Child
    }
}