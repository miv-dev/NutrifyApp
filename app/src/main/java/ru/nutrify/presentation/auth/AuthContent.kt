package ru.nutrify.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.nutrify.presentation.login.LoginContent
import ru.nutrify.presentation.root.RootComponent

@Composable
fun AuthContent(component: AuthComponent) {
    Children(stack = component.stack) { child ->
        when (val instance = child.instance) {
            is AuthComponent.Child.Login -> LoginContent(instance.component)
            is AuthComponent.Child.Register -> {}
        }
    }
}