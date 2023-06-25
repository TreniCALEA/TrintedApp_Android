package com.trenicalea.trintedapp.viewmodels


import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.UtenteControllerApi
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.Indirizzo
import com.trenicalea.trintedapp.models.UtenteBasicDto
import com.trenicalea.trintedapp.models.UtenteCompletionDto
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.models.UtenteRegistrationDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UserUpdateState(
    val nome: String = "",
    val cognome: String = "",
    val immagine: Bitmap? = null,
    val citta: String = "",
    val via: String = "",
    val civico: Int = 0
)

class UtenteViewModel : ViewModel() {

    private val _userUpdateState = MutableStateFlow(UserUpdateState())
    val userUpdateState: StateFlow<UserUpdateState> = _userUpdateState.asStateFlow()

    private val _userApi: UtenteControllerApi = UtenteControllerApi()
    val prefix: MutableState<String> = mutableStateOf("")
    val userList: MutableState<List<UtenteBasicDto>> = mutableStateOf(listOf())

    fun register(username: String, email: String, password: String) {
        _userApi.add(UtenteRegistrationDto(username, email, password))
    }

    fun updateNome(nome: String) {
        _userUpdateState.value = _userUpdateState.value.copy(
            nome = nome
        )
    }

    fun updateCognome(cognome: String) {
        _userUpdateState.value = _userUpdateState.value.copy(
            cognome = cognome
        )
    }

    fun updateImmagine(immagine: Bitmap?) {
        _userUpdateState.value = _userUpdateState.value.copy(
            immagine = immagine
        )
    }

    fun updateCitta(citta: String) {
        _userUpdateState.value = _userUpdateState.value.copy(
            citta = citta
        )
    }

    fun updateCivico(civico: Int) {
        _userUpdateState.value = _userUpdateState.value.copy(
            civico = civico
        )
    }

    fun updateVia(via: String) {
        _userUpdateState.value = _userUpdateState.value.copy(
            via = via
        )
    }

    fun updateUser(
        appwrite: AppwriteConfig,
        authViewModel: AuthViewModel,
        nome: String,
        cognome: String,
        image: Bitmap?,
        indirizzo: Indirizzo
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            _userApi.update(
                authViewModel.loggedInUser.value!!.id,
                UtenteCompletionDto(nome, cognome, image, indirizzo),
                appwrite.account.createJWT().jwt
            )
        }
    }

    fun getUser(id: Long): UtenteDto {
        return _userApi.getById(id)
    }

    fun getUserByUsernameLike() {
        userList.value = _userApi.getAllByUsernameLike(prefix.value).toList()
    }

    fun getByCredenzialiEmail(credenzialiEmail: String): UtenteDto {
        return _userApi.getByCredenzialiEmail(credenzialiEmail)
    }

    fun deleteProfile(id: Long, appwrite: AppwriteConfig) {
        CoroutineScope(Dispatchers.IO).launch { _userApi.delete(id, appwrite.account.createJWT().jwt) }
    }


}