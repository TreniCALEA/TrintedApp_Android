package com.trenicalea.trintedapp

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.viewmodels.AuthViewModel
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel

@Composable
fun AuthActivity(
    activity: ComponentActivity,
    appwrite: AppwriteConfig,
    authViewModel: AuthViewModel,
    utenteViewModel: UtenteViewModel
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {

        var usernameProvider by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }

        val authState by authViewModel.authState.collectAsState()
        val registrationError by remember { derivedStateOf { authState.emailHasError || authState.usernameHasError || authState.passwordHasError } }
        val loginError by remember { derivedStateOf { authState.emailHasError || authState.passwordHasError } }

        Column {

            // Headline text
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                    text = if (!authViewModel.login.value) "Iscriviti!" else "Login!",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 25.sp)
                )
            }

            // Username text field
            if (!authViewModel.login.value) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        isError = authState.usernameHasError,
                        value = authState.username,
                        onValueChange = { authViewModel.updateUsername(it) },
                        label = { Text(stringResource(R.string.usernameLabel)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = stringResource(R.string.personIcon)
                            )
                        },
                    )
                }
            }

            // Email text field
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    isError = authState.emailHasError,
                    value = authState.email,
                    onValueChange = { authViewModel.updateEmail(it) },
                    label = { Text(stringResource(R.string.emailLabel)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = stringResource(R.string.emailIcon)
                        )
                    },
                )
            }

            // Password text field
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = authState.password,
                    onValueChange = { authViewModel.updatePassword(it) },
                    isError = authState.passwordHasError,
                    label = { Text(stringResource(R.string.passwordLabel)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock, contentDescription = stringResource(
                                id = R.string.passwordIcon
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        // Please provide localized description for accessibility services
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }
                    })
            }

            // Tasto login/registrati
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Button(onClick = {
                    if (!authViewModel.login.value) {
                        if (!registrationError) authViewModel.registerWithCredentials(
                            authState.username, authState.email, authState.password
                        )
                    } else {
                        if (!loginError) authViewModel.emailLogin(
                            authState.email, authState.password, appwrite, utenteViewModel
                        )
                    }
                }) {
                    Text(text = if (!authViewModel.login.value) "Registrati" else "Login")
                }
            }

            // Another small text
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Oppure, " + if (!authViewModel.login.value) "hai già un profilo?" else "vuoi registrarti?")
            }

            // Bottone che switcha tra login/registrazione
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth()
            ) {
                Button(onClick = {
                    authViewModel.login.value = !authViewModel.login.value
                    usernameProvider = ""
                }) {
                    Text(text = if (!authViewModel.login.value) "Login" else "Registrati")
                }
            }

            // Another small text
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 5.dp)
            ) {
                Text(text = "Oppure, " + if (!authViewModel.login.value) "registrati con:" else "accedi con:")
            }

            // Username per google/facebook
            if (!authViewModel.login.value) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    OutlinedTextField(
                        isError = authState.usernameProviderHasError,
                        value = authState.usernameProvider,
                        onValueChange = { authViewModel.updateUsernameProvider(it) },
                        label = { Text(stringResource(R.string.usernameLabel)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = stringResource(R.string.personIcon)
                            )
                        },
                    )
                }
            }

            // Facebook button
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(colors = ButtonDefaults.buttonColors(containerColor = Color(59, 89, 152)),
                    onClick = {
                        if (!authState.usernameProviderHasError) {
                            if (!authViewModel.login.value) {
                                authViewModel.providerLogin(
                                    appwrite,
                                    activity,
                                    "facebook",
                                    utenteViewModel,
                                    authState.usernameProvider
                                )
                            }
                        }
                        if (authViewModel.login.value) {
                            authViewModel.providerLogin(
                                appwrite, activity, "facebook", utenteViewModel
                            )
                        }
                    }) {
                    Text(
                        text = if (!authViewModel.login.value) "Registrati con Facebook" else "Login con Facebook",
                    )
                }
            }

            // Google button
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(colors = ButtonDefaults.buttonColors(containerColor = Color(219, 68, 55)),
                    onClick = {
                        if (!authState.usernameProviderHasError) {
                            if (!authViewModel.login.value) {
                                authViewModel.providerLogin(
                                    appwrite,
                                    activity,
                                    "google",
                                    utenteViewModel,
                                    authState.usernameProvider
                                )
                            }
                        }
                        if (authViewModel.login.value) {
                            authViewModel.providerLogin(
                                appwrite, activity, "google", utenteViewModel
                            )
                        }
                    }) {
                    Text(
                        text = if (!authViewModel.login.value) "Registrati con Google" else "Login con Google",
                        modifier = Modifier.padding(horizontal = 7.dp)
                    )
                }
            }
        }
    }
}
