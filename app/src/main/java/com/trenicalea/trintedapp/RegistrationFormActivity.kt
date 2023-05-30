package com.trenicalea.trintedapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.trenicalea.trintedapp.viewmodels.UtenteRegistrationViewModel


@Composable
fun RegistrationFormActivity(registrationViewModel: UtenteRegistrationViewModel = UtenteRegistrationViewModel()) {
    val userState by registrationViewModel.userState.collectAsState()
    val showError by remember { derivedStateOf { userState.validUsername || userState.validPassword } }

}
