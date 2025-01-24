package ru.nutrify.presentation.home

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface HomeComponent {

    val stack: Value<ChildStack<*, Child>>

    fun navigateToMain()

    fun navigateToAccount()

    fun navigateToSearch()



    sealed interface Child {
        data object Main : Child
        data object Account : Child
        data object Search : Child
    }
}