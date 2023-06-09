package com.trenicalea.trintedapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.ArticoloControllerApi

class ArticoloViewModel : ViewModel() {
    private val _articoloApi: ArticoloControllerApi = ArticoloControllerApi()

    fun getImage(id: Long): String? {
        return _articoloApi.getById2(id).immagini?.first()
    }
}