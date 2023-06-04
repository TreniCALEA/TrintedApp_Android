package com.trenicalea.trintedapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.OrdineControllerApi
import com.trenicalea.trintedapp.models.UtenteDto

class OrdineViewModel : ViewModel() {
    private val _orderApi : OrdineControllerApi = OrdineControllerApi()
}