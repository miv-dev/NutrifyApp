package ru.nutrify.presentation.root

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.nutrify.presentation.auth.AuthContent
import ru.nutrify.presentation.home.HomeContent
import ru.nutrify.ui.theme.NutrifyTheme

@Composable
fun RootContent(component: RootComponent) {
    NutrifyTheme(darkTheme = false) {
        Children(stack = component.stack) { child ->
            when (val instance = child.instance) {
                is RootComponent.Child.Auth -> AuthContent(instance.component)
                is RootComponent.Child.Home -> HomeContent(instance.component)
                RootComponent.Child.Loading -> LoadingScreen()
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Scaffold {
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}