package com.trenicalea.trintedapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.OrdineControllerApi
import com.trenicalea.trintedapp.models.OrdineDto

class OrdineViewModel : ViewModel() {
    private val _orderApi : OrdineControllerApi = OrdineControllerApi()

    fun getByVenditore(id: Long): Array<OrdineDto> {
        return _orderApi.getByVenditore(id)
    }

    fun getByAcquirente(id: Long): Array<OrdineDto> {
        return _orderApi.getByAcquirente(id)
    }
}