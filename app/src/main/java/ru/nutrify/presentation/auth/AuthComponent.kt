package ru.nutrify.presentation.auth

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.nutrify.presentation.login.LoginComponent
import ru.nutrify.presentation.register.RegisterComponent

interface AuthComponent {

    val stack: Value<ChildStack<*, Child>>

    fun navigateToLogin()

    fun navigateToRegister()

    interface Child {
        data class Login(val component: LoginComponent): Child
        data class Register(val component: RegisterComponent): Child
    }

}