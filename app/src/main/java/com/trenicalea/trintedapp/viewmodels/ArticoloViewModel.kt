package com.trenicalea.trintedapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.ArticoloControllerApi
import com.trenicalea.trintedapp.models.ArticoloDto

class ArticoloViewModel : ViewModel() {
    private val _articoloApi: ArticoloControllerApi = ArticoloControllerApi()

    fun getArticolo(id: Long): ArticoloDto{
        return _articoloApi.getById2(id)
    }

    fun getImage(id: Long): String? {
        return _articoloApi.getById2(id).immagini?.first()
    }
}