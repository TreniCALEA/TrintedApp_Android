package com.trenicalea.trintedapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.OrdineControllerApi

class OrdineViewModel : ViewModel() {
    private val _orderApi : OrdineControllerApi = OrdineControllerApi()
}