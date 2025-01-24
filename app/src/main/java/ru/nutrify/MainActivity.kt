package ru.nutrify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import ru.nutrify.presentation.root.DefaultRootComponent
import ru.nutrify.presentation.root.RootContent
import ru.nutrify.ui.theme.NutrifyTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as NutrifyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val rootComponent = rootComponentFactory.create(defaultComponentContext())
        setContent {
            RootContent(rootComponent)
        }
    }
}
