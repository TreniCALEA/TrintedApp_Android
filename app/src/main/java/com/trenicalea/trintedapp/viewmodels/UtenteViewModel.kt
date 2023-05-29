package com.trenicalea.trintedapp.viewmodels

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.UtenteControllerApi
import com.trenicalea.trintedapp.models.UtenteBasicDto

class UtenteViewModel : ViewModel() {

    private val _userApi: UtenteControllerApi = UtenteControllerApi()

    private val _listUser: Array<UtenteBasicDto> = arrayOf()

    val users = _listUser



}