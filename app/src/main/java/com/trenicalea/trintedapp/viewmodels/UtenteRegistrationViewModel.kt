package com.trenicalea.trintedapp.viewmodels

import com.trenicalea.trintedapp.apis.UtenteControllerApi
import com.trenicalea.trintedapp.models.UtenteBasicDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserForm (
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val validPassword: Boolean = password.length >= 8,
)

class UtenteRegistrationViewModel {

    private val _userState = MutableStateFlow(UserForm())
    val userState: StateFlow<UserForm> = _userState.asStateFlow()

    fun updateUsername(username: String) {
        _userState.value = _userState.value.copy(
            username = username,
        )
    }

    fun updateEmail(email: String) {
        _userState.value = _userState.value.copy(
            email = email,
        )
    }

    fun updatePassword(password: String) {
        val hasError: Boolean = password.length >= 8
        _userState.value = _userState.value.copy(
            password = password,
            validPassword = hasError,
        )
    }

}