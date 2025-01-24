package ru.nutrify.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.nutrify.presentation.account.AccountContent
import ru.nutrify.presentation.main.MainContent
import ru.nutrify.presentation.search.SearchContent


@Composable
fun HomeContent(component: HomeComponent) {
    val stack by component.stack.subscribeAsState()
    Scaffold(
        bottomBar = {
            val selectedItem = stack.active.instance
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedItem == HomeComponent.Child.Main) {
                                Icons.Filled.Home
                            } else {
                                Icons.Outlined.Home
                            },
                            contentDescription = "Main"
                        )
                    },
                    selected = selectedItem == HomeComponent.Child.Main,
                    onClick = component::navigateToMain
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedItem == HomeComponent.Child.Search) {
                                Icons.Filled.Search
                            } else {
                                Icons.Outlined.Search
                            },
                            contentDescription = "Search"
                        )
                    },
                    selected = selectedItem == HomeComponent.Child.Search,
                    onClick = component::navigateToSearch
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedItem == HomeComponent.Child.Account) {
                                Icons.Filled.Person
                            } else {
                                Icons.Outlined.Person
                            },
                            contentDescription = "Account"
                        )
                    },
                    selected = selectedItem == HomeComponent.Child.Account,
                    onClick = component::navigateToAccount
                )
            }
        }
    ) {
        Column(Modifier.padding(it)) {
            Children(stack = component.stack) { child ->
                when (val instance = child.instance) {
                    HomeComponent.Child.Account -> AccountContent()
                    HomeComponent.Child.Main -> MainContent()
                    HomeComponent.Child.Search -> SearchContent()
                }
            }
        }
    }
}