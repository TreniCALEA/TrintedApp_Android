package com.trenicalea.trintedapp.viewmodels

import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.UtenteControllerApi
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.models.UtenteRegistrationDto
import io.appwrite.Client
import io.appwrite.services.Account
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture

data class AuthState(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val usernameProvider: String = "",
    val usernameHasError: Boolean = !UtenteRegistrationDto.validateCredenzialiUsername(username),
    val emailHasError: Boolean = !UtenteRegistrationDto.validateCredenzialiEmail(email),
    val passwordHasError: Boolean = !UtenteRegistrationDto.validateCredenzialiPassword(password),
    val usernameProviderHasError: Boolean = !UtenteRegistrationDto.validateCredenzialiUsername(
        usernameProvider
    ),
)

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _userApi: UtenteControllerApi = UtenteControllerApi()

    val isLogged: MutableState<Boolean> = mutableStateOf(false)
    val loading: MutableState<Boolean> = mutableStateOf(true)
    private val providerLoginCompleted = CompletableDeferred(true)
    val login: MutableState<Boolean> = mutableStateOf(false)
    val loggedInUser: MutableState<UtenteDto?> = mutableStateOf(null)
    val isVerified: MutableState<Boolean> = mutableStateOf(false)


    fun logout(appwrite: AppwriteConfig) {
        val client: Client = Client(appwrite.appContext)
            .setEndpoint(appwrite.endpoint)
            .setProject(appwrite.projectId)

        val account = Account(client)
        CoroutineScope(Dispatchers.IO).launch {
            account.deleteSession("current")
        }.invokeOnCompletion { isLogged.value = false }
    }

    fun emailLogin(
        email: String,
        password: String,
        appwrite: AppwriteConfig,
        utenteViewModel: UtenteViewModel,
        banned: MutableState<Boolean>
    ) {
        if (checkBan(banned, email)) {
            return
        }

        val client: Client = Client(appwrite.appContext)
            .setEndpoint(appwrite.endpoint)
            .setProject(appwrite.projectId)

        val account = Account(client)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                account.createEmailSession(email, password)
                loggedInUser.value = utenteViewModel.getByCredenzialiEmail(email)
                println("Utente emailLogin: ${loggedInUser.value!!.nome}")
                isLogged.value = true
            } catch (e: Exception) {
                isLogged.value = false
            }
        }.invokeOnCompletion {
            login.value = true
        }
    }

    fun isVerified(appwrite: AppwriteConfig): Boolean {
        val client: Client = Client(appwrite.appContext)
            .setEndpoint(appwrite.endpoint)
            .setProject(appwrite.projectId)

        val account = Account(client)
        val verified: CompletableFuture<Boolean> = CompletableFuture()
        CoroutineScope(Dispatchers.IO).launch {
            verified.complete(account.get().emailVerification)
        }
        return verified.join()
    }

    fun verifyEmail(appwrite: AppwriteConfig) {
        val client: Client = Client(appwrite.appContext)
            .setEndpoint(appwrite.endpoint)
            .setProject(appwrite.projectId)

        val account = Account(client)

        CoroutineScope(Dispatchers.IO).launch {
            account.createVerification("http://localhost")
        }.invokeOnCompletion { isVerified.value = true }
    }

    fun checkLogin(
        appwrite: AppwriteConfig,
        utenteViewModel: UtenteViewModel,
        banned: MutableState<Boolean>
    ) {
        val client: Client = Client(appwrite.appContext)
            .setEndpoint(appwrite.endpoint)
            .setProject(appwrite.projectId)

        val account = Account(client)

        if (!isLogged.value) {
            println("")
            CoroutineScope(Dispatchers.IO).launch {
                providerLoginCompleted.await()
                try {
                    println(account.get().email)
                    println("[i] Session: \n" + account.getSession("current"))
                    try {
                        isVerified.value = account.get().emailVerification
                        loggedInUser.value =
                            utenteViewModel.getByCredenzialiEmail(account.get().email)
                        println("Utente checkLogged: ${loggedInUser.value!!.credenzialiEmail}")
                        println("Controllo per il ban")
                        checkBan(
                            banned,
                            loggedInUser.value!!.credenzialiEmail
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    isLogged.value = true
                } catch (e: Exception) {
                    println("[i] Session invalid!")
                    isLogged.value = false
                }
            }.invokeOnCompletion {
                loading.value = false
            }
        } else {
            println("[D] User is already logged in as ${loggedInUser.value!!.credenzialiEmail}, no need to re-check.")
        }
    }

    fun providerLogin(
        appwrite: AppwriteConfig,
        activity: ComponentActivity,
        provider: String,
        utenteViewModel: UtenteViewModel,
        usernameProvider: String? = null,
        banned: MutableState<Boolean>
    ) {
        val client: Client = Client(appwrite.appContext)
            .setEndpoint(appwrite.endpoint)
            .setProject(appwrite.projectId)

        val account: Account = Account(client)
        // Show loading screen
        loading.value = true

        // Set providerLoginCompleted to false to stop other jobs
        providerLoginCompleted.complete(false)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Create OAuth2 Session on Appwrite
                account.createOAuth2Session(
                    activity,
                    provider,
                    "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/success",
                    "appwrite-callback-645d4c2c39e030c6f6ba://cloud.appwrite.io/auth/oauth2/failure"
                )
                // If an username is passed as a parameter, means that the user is about to register
                if (usernameProvider != null) {
                    println("[D] User is registering with OAuth2! Username: $usernameProvider.")
                    try {
                        registerWithCredentials(
                            usernameProvider,
                            account.get().email,
                            account.get().id,
                            banned
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    // ...otherwise, the user is just logging in using OAuth2.
                } else {
                    println("[D] User is just logging in, proceed with populating loggedInUser.")
                }
            } catch (e: Exception) {
                println("[i] Login/Register with $provider cancelled.")
                isLogged.value = false
            }
        }.invokeOnCompletion {
            checkLogin(appwrite, utenteViewModel, banned)
            // If needed, tell checkLogged to proceed without any further issue
            providerLoginCompleted.complete(true)
        }
    }

    fun registerWithCredentials(
        username: String,
        email: String,
        password: String,
        banned: MutableState<Boolean>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = UtenteViewModel()
                user.register(username, email, password)
            } catch (e: Exception) {
                println("[i] Register failed!")
                println("[i] Exception: $e")
                isLogged.value = false
            }
        }.invokeOnCompletion {
            loading.value = true
        }
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

    fun checkBan(banned: MutableState<Boolean>, email: String): Boolean {
        try {
            banned.value = _userApi.checkBan(email) == "OK"
            println("Banned value: $banned.value")
            return banned.value
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}