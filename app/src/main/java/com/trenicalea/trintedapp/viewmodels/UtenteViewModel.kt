package com.trenicalea.trintedapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.UtenteControllerApi
import com.trenicalea.trintedapp.appwrite.account
import com.trenicalea.trintedapp.models.PageUtenteBasicDto
import com.trenicalea.trintedapp.models.UtenteBasicDto
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.models.UtenteRegistrationDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UtenteViewModel : ViewModel() {

    private val _userApi: UtenteControllerApi = UtenteControllerApi()
    private val _listUser: Array<UtenteBasicDto> = _userApi.all()
    val users = _listUser

    fun isLogged(): Boolean {
        var logged: Boolean = false
        val job = CoroutineScope(Dispatchers.IO).launch {
            logged = account.getSession("current").current
        }
        return logged
    }

    fun register(username: String, email: String, password: String) {
        _userApi.add(UtenteRegistrationDto(username, email, password))
    }

    fun getUser(id: Long): UtenteDto? {
        return _userApi.getById(id)
    }

    fun getUserByUsernameLikePaged(prefix: String, page: Int): PageUtenteBasicDto? {
        return _userApi.getAllByUsernameLikePaged(prefix, page)
    }

}