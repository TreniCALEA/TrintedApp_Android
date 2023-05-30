package com.trenicalea.trintedapp.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.UtenteControllerApi
import com.trenicalea.trintedapp.models.PageUtenteBasicDto
import com.trenicalea.trintedapp.models.UtenteBasicDto
import com.trenicalea.trintedapp.models.UtenteDto

class UtenteViewModel : ViewModel() {

    private val _userApi: UtenteControllerApi = UtenteControllerApi()
    private val _listUser: Array<UtenteBasicDto> = _userApi.all()
    val users = _listUser

    fun getUser(id: Long): UtenteDto? {
        return _userApi.getById(id)
    }

    fun getUserByUsernameLikePaged(prefix: String, page: Int): PageUtenteBasicDto? {
        return _userApi.getAllByUsernameLikePaged(prefix, page)
    }

}