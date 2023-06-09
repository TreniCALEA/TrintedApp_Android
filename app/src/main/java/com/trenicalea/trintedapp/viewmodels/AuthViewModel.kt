package com.trenicalea.trintedapp.viewmodels

import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.models.UtenteRegistrationDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthState(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val usernameProvider: String = "",
    val usernameHasError: Boolean = !UtenteRegistrationDto.validateCredenzialiUsername(username),
    val emailHasError: Boolean = !UtenteRegistrationDto.validateCredenzialiEmail(email),
    val passwordHasError: Boolean = !UtenteRegistrationDto.validateCredenzialiPassword(password),
    val usernameProviderHasError: Boolean = !UtenteRegistrationDto.validateCredenzialiUsername(usernameProvider),
)

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

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
        utenteViewModel: UtenteViewModel,
        usernameProvider: String? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appwrite.account.createOAuth2Session(
                    activity,
                    provider,
                    "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/success",
                    "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/failure"
                )
                try {
                    loggedInUser.value =
                        utenteViewModel.getByCredenzialiEmail(appwrite.account.get().email)
                    println("Utente checkLogged: ${loggedInUser.value!!.credenzialiEmail}")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                isLogged.value = true
            } catch (e: Exception) {
                println("[i] Login with $provider cancelled.")
                isLogged.value = false;
            }
        }.invokeOnCompletion {
            CoroutineScope(Dispatchers.IO).launch {
                if(usernameProvider != null) {
                    registerWithCredentials(usernameProvider, appwrite.account.get().email, appwrite.account.get().id)
                }
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

    fun updateUsername(username: String) {
        val hasError = !UtenteRegistrationDto.validateCredenzialiUsername(username)
        _authState.value = _authState.value.copy(
            username = username,
            usernameHasError = hasError
        )
    }

    fun updateEmail(email: String) {
        val hasError = !UtenteRegistrationDto.validateCredenzialiEmail(email)
        _authState.value = _authState.value.copy(
            email = email,
            emailHasError = hasError
        )
    }

    fun updatePassword(password: String) {
        val hasError = !UtenteRegistrationDto.validateCredenzialiUsername(password)
        _authState.value = _authState.value.copy(
            password = password,
            passwordHasError = hasError
        )
    }

    fun updateUsernameProvider(usernameProvider: String) {
        val hasError = !UtenteRegistrationDto.validateCredenzialiUsername(usernameProvider)
        _authState.value = _authState.value.copy(
            usernameProvider = usernameProvider,
            usernameProviderHasError = hasError
        )
    }
}