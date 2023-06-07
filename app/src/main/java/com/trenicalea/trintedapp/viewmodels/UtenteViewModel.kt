package com.trenicalea.trintedapp.viewmodels


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.Config
import com.trenicalea.trintedapp.apis.UtenteControllerApi
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.PageUtenteBasicDto
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.models.UtenteRegistrationDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UtenteViewModel : ViewModel() {

    private val _userApi: UtenteControllerApi = UtenteControllerApi()
    val isChecked: MutableState<Boolean> = mutableStateOf(false)

    fun register(username: String, email: String, password: String) {
        _userApi.add(UtenteRegistrationDto(username, email, password))
    }

    fun getUser(id: Long): UtenteDto {
        return _userApi.getById(id)
    }

//    fun getUserIdAppwrite(appwrite: AppwriteConfig): String {
//        CoroutineScope(Dispatchers.IO).launch {
//            appwrite.account.getSession("current").userId
//        }
//    }

    fun checkVerified(appwrite: AppwriteConfig) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appwrite.account.createVerification("http://${Config.ip}/account_verification.html")
            } catch (e: Exception) {
              isChecked.value = false
            }
        }.invokeOnCompletion { isChecked.value = true }
    }

    fun getUserByUsernameLikePaged(prefix: String, page: Int): PageUtenteBasicDto {
        return _userApi.getAllByUsernameLikePaged(prefix, page)
    }

}