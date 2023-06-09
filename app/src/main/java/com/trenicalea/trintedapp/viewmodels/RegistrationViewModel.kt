package com.trenicalea.trintedapp.viewmodels

import android.app.AlertDialog
import android.content.Context
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.UtenteDto

class RegistrationViewModel : ViewModel() {
    val isLogged: MutableState<Boolean> = mutableStateOf(false)
    val loading: MutableState<Boolean> = mutableStateOf(true)
    val login: MutableState<Boolean> = mutableStateOf(false)
    val loggedInUser: MutableState<UtenteDto?> = mutableStateOf(null)

    fun logout(appwrite: AppwriteConfig) {
        CoroutineScope(Dispatchers.IO).launch {
            appwrite.account.deleteSession("current")
        }.invokeOnCompletion { isLogged.value = false }
    }

    fun emailLogin(
        email: String,
        password: String,
        appwrite: AppwriteConfig,
        utenteViewModel: UtenteViewModel
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appwrite.account.createEmailSession(email, password)
                loggedInUser.value = utenteViewModel.getByCredenzialiEmail(email)
                println("Utente emailLogin: ${loggedInUser.value!!.nome}")
                isLogged.value = true
            } catch (e: Exception) {
                isLogged.value = false
            }
        }.invokeOnCompletion { login.value = true }
    }

    fun checkLogged(appwrite: AppwriteConfig, utenteViewModel: UtenteViewModel) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println(appwrite.account.get().email)
                println("[i] Session: \n" + appwrite.account.getSession("current"))
                try {
                    loggedInUser.value =
                        utenteViewModel.getByCredenzialiEmail(appwrite.account.get().email)
                    println("Utente checkLogged: ${loggedInUser.value!!.credenzialiEmail}")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                isLogged.value = true;
            } catch (e: Exception) {
                println("[i] Session invalid!")
                isLogged.value = false;
            }
        }.invokeOnCompletion { loading.value = false }
    }

    fun providerLogin(
        appwrite: AppwriteConfig,
        activity: ComponentActivity,
        provider: String,
        utenteViewModel: UtenteViewModel
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appwrite.account.createOAuth2Session(
                    activity,
                    provider,
                    "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/success",
                    "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/failure"
                )

                loggedInUser.value =
                    utenteViewModel.getByCredenzialiEmail(appwrite.account.get().email)
                println("Utente providerLogin: ${loggedInUser.value!!.nome}")
                isLogged.value = true
            } catch (e: Exception) {
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
            } catch (e: Exception) {
                println("[i] Register failed!")
                println("[i] Exception: $e")
                isLogged.value = false
            }
        }.invokeOnCompletion { loading.value = true }
    }
}