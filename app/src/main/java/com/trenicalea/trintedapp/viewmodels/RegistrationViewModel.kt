package com.trenicalea.trintedapp.viewmodels

import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.trenicalea.trintedapp.appwrite.AppwriteConfig

class RegistrationViewModel: ViewModel() {
    val isLogged: MutableState<Boolean> = mutableStateOf(false);
    val loading: MutableState<Boolean> = mutableStateOf(true);

    fun checkLogged(appwrite: AppwriteConfig) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("[i] Session: \n" + appwrite.account.getSession("current"))
                isLogged.value = true;
            } catch(e: Exception) {
                println("[i] Session invalid!")
                isLogged.value = false;
            }
        }.invokeOnCompletion { loading.value = false }
    }

    fun providerLogin(appwrite: AppwriteConfig, activity: ComponentActivity, provider: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appwrite.account.createOAuth2Session(activity, provider, "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/success",
                    "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/failure")
                isLogged.value = true
            } catch(e: Exception) {
                println("[i] Login with $provider cancelled.")
                isLogged.value = false;
            }
        }
    }

    fun registerWithCredentials(username: String, email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = UtenteViewModel()
                user.register(username, email, password)
            } catch(e: Exception) {
                println("[i] Register failed!")
                println("[i] Exception: $e")
                isLogged.value = false
            }
        }.invokeOnCompletion { loading.value = true }
    }
}