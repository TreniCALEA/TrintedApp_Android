package com.trenicalea.trintedapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.OrdineControllerApi
import com.trenicalea.trintedapp.models.Ordine
import com.trenicalea.trintedapp.models.OrdineDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrdineViewModel(val id: Long) : ViewModel() {

    init {
        getByAcquirente(id)
        getByVenditore(id)
    }

    private val _orderApi: OrdineControllerApi = OrdineControllerApi()

    var ordersGetByVenditore: Array<OrdineDto> = emptyArray()
    var ordersGetByAcquirente: Array<OrdineDto> = emptyArray()

    fun getByVenditore(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ordersGetByVenditore = _orderApi.getByVenditore(id)
            } catch (e: Exception) {
                ordersGetByVenditore = emptyArray()
            }
        }
    }

    fun getByAcquirente(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ordersGetByAcquirente = _orderApi.getByAcquirente(id)
            } catch (e: Exception) {
                ordersGetByAcquirente = emptyArray()
            }
        }
    }
}