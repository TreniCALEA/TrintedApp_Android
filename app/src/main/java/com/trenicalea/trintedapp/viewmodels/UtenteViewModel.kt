package com.trenicalea.trintedapp.viewmodels


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.Config
import com.trenicalea.trintedapp.apis.UtenteControllerApi
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.UtenteBasicDto
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.models.UtenteRegistrationDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UtenteViewModel : ViewModel() {

    private val _userApi: UtenteControllerApi = UtenteControllerApi()
    val isChecked: MutableState<Boolean> = mutableStateOf(false)
    val prefix: MutableState<String> = mutableStateOf("")
    val userList: MutableState<List<UtenteBasicDto>> = mutableStateOf(listOf())

    fun register(username: String, email: String, password: String) {
        _userApi.add(UtenteRegistrationDto(username, email, password))
    }

    fun getUser(id: Long): UtenteDto {
        return _userApi.getById(id)
    }

    fun checkVerified(appwrite: AppwriteConfig) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appwrite.account.createVerification("http://${Config.ip}/account_verification.html")
            } catch (e: Exception) {
                isChecked.value = false
            }
        }.invokeOnCompletion { isChecked.value = true }
    }

    fun getUserByUsernameLike(){
        userList.value = _userApi.getAllByUsernameLike(prefix.value).toList()
    }



    fun getByCredenzialiEmail(credenzialiEmail: String): UtenteDto {
        return _userApi.getByCredenzialiEmail(credenzialiEmail)
    }

}