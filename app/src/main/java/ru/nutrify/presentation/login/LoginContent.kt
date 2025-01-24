package ru.nutrify.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dialpad
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(component: LoginComponent) {
    val model by component.model.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showPassword by remember { mutableStateOf(false) }
    LaunchedEffect(model.loginState) {
        val state = model.loginState
        if (state is LoginStore.State.LoginState.Error) {
            snackbarHostState.showSnackbar(state.error)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Вход") })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }

    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            OutlinedTextField(
                value = model.username,
                onValueChange = component::changeUsername,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                isError = model.usernameError != null,
                supportingText = { model.usernameError?.let { Text(text = it) } },
                leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = "Email") },
                placeholder = { Text(text = "Введите Username") },
                label = { Text(text = "Username") })
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = model.password,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                isError = model.passwordError != null,
                supportingText = { model.passwordError?.let { Text(text = it) } },
                onValueChange = component::changePassword,
                leadingIcon = { Icon(Icons.Rounded.Dialpad, contentDescription = "Password") },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        val icon = if (showPassword) {
                            Icons.Rounded.Visibility

                        } else {
                            Icons.Rounded.VisibilityOff
                        }
                        Icon(icon, contentDescription = "Show Password")
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                label = { Text(text = "Пароль") },
                placeholder = { Text(text = "Введите пароль") },
            )

            Spacer(modifier = Modifier.weight(1f))
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = component::onLoginClick,
                enabled = model.username.isNotEmpty() && model.password.isNotEmpty()
            ) {
                Text(text = "Войти")
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Нет аккаунта?",
                )
                FilledTonalButton(onClick = component::onRegisterClick) {
                    Text(text = "Создать")
                }
            }
        }
    }
}