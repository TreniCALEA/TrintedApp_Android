package com.trenicalea.trintedapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.OrdineControllerApi
import com.trenicalea.trintedapp.models.ArticoloDto
import com.trenicalea.trintedapp.models.Indirizzo
import com.trenicalea.trintedapp.models.Ordine

class CheckoutViewModel : ViewModel() {
    private var _ordineApi: OrdineControllerApi = OrdineControllerApi()
    private var ordine: Ordine = Ordine()

    fun confirmOrder(acquirente: Long, articoloDto: ArticoloDto, indirizzo: Indirizzo){
       _ordineApi.add2(acquirente, articoloDto, indirizzo)
    }

}