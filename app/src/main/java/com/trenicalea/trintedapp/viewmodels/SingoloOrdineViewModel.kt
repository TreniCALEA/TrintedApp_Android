package com.trenicalea.trintedapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.OrdineControllerApi
import com.trenicalea.trintedapp.models.OrdineDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingoloOrdineViewModel(val id: Long): ViewModel() {
    private val _orderApi : OrdineControllerApi = OrdineControllerApi()
    var ordine : OrdineDto? = null

    private fun getOrder(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            ordine = _orderApi.getById1(id)
        }
    }
}
