package ru.nutrify.presentation.ext

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

fun ComponentContext.componentScope() = CoroutineScope(
    Main + SupervisorJob()
).apply {
    lifecycle.doOnDestroy {
        cancel()
    }
}