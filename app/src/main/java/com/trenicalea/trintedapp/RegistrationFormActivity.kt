package com.trenicalea.trintedapp

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun RegistrationFormActivity(activity: ComponentActivity, appwrite: AppwriteConfig) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), modifier = Modifier
        .padding(12.dp)
        .fillMaxWidth(),
    ) {

        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }

        Column {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Iscriviti con l'e-mail",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 25.sp)
                )
            }
            
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = username,
                    onValueChange = { username = it },
                    label = { Text(stringResource(R.string.usernameLabel)) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = stringResource(R.string.personIcon)) },
                )
            }
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = email,
                            onValueChange = { email = it},
                            label = { Text(stringResource(R.string.emailLabel)) },
                            leadingIcon = { Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = stringResource(R.string.emailIcon)
                            )},
                )
            }
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = password,
                                  onValueChange = { password = it },
                                  label = { Text(stringResource(R.string.passwordLabel)) },
                                  leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(
                                      id = R.string.passwordIcon
                                  )) },
                                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                  visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                  trailingIcon = {
                                      val image = if (passwordVisible)
                                          Icons.Filled.Visibility
                                      else Icons.Filled.VisibilityOff

                                      // Please provide localized description for accessibility services
                                      val description = if (passwordVisible) "Hide password" else "Show password"

                                      IconButton(onClick = { passwordVisible = !passwordVisible}){
                                          Icon(imageVector  = image, description)
                                      }
                                  }
                )
            }
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    var user = UtenteViewModel()
                    user.register(username, email, password)
                }) {
                    Text(text = "Registrati")
                }
            }
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Oppure accedi con:")
            }
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(59, 89, 152)),
                    onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            appwrite.account.createOAuth2Session(activity, "facebook", "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/success",
                                "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/failure")
                        } catch(e: Exception) {
                            println("[i] Login with facebook cancelled.")
                        }
                    }
                }) {
                    Text(stringResource(id = R.string.facebookLogin))
                }
            }
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(219, 68, 55)),
                    onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            appwrite.account.createOAuth2Session(activity, "google", "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/success",
                                "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/failure")
                        } catch(e: Exception) {
                            println("[i] Login with google cancelled.")
                        }
                    }
                }) {
                    Text(stringResource(id = R.string.googleLogin), modifier = Modifier.padding(horizontal = 7.dp))
                }
            }
        }
    }
}
