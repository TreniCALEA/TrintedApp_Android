package com.trenicalea.trintedapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.RecensioneControllerApi
import com.trenicalea.trintedapp.models.Recensione

class RecensioneViewModel : ViewModel() {

    private val _reviewApi: RecensioneControllerApi = RecensioneControllerApi()
    var reviewList: MutableState<List<Recensione>> = mutableStateOf(listOf())

    fun getUserReview(authViewModel: AuthViewModel){
        reviewList.value = _reviewApi.findAll(authViewModel.loggedInUser.value!!.id).toList()
    }

}