package ru.nutrify.presentation.register

import kotlinx.coroutines.flow.StateFlow

interface RegisterComponent {
    val model: StateFlow<RegisterStore.State>


    fun changeEmail(email: String)

    fun changePassword(password: String)

    fun changeUsername(username: String)

}