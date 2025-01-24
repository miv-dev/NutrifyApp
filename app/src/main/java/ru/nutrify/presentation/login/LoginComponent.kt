package ru.nutrify.presentation.login

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow

interface LoginComponent {
    
    val model: StateFlow<LoginStore.State>


    fun changeUsername(username: String)

    fun changePassword(password: String)

    fun onLoginClick()

    fun onRegisterClick()

}