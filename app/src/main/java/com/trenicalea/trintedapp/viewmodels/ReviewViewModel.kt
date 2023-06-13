package com.trenicalea.trintedapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.RecensioneControllerApi
import com.trenicalea.trintedapp.models.Recensione
import com.trenicalea.trintedapp.models.RecensioneDto
import com.trenicalea.trintedapp.models.UtenteDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ReviewState(
    var description: String = "",
    var rating: Float = 0.0f,
    val descriptionHasError: Boolean = !RecensioneDto.validateDescrizione(description)
)

class ReviewViewModel : ViewModel() {

    private val _reviewState = MutableStateFlow(ReviewState())
    val reviewState: StateFlow<ReviewState> = _reviewState.asStateFlow()


    private val _reviewApi: RecensioneControllerApi = RecensioneControllerApi()
    var reviewList: MutableState<List<Recensione>> = mutableStateOf(listOf())

    fun getUserReview(utenteDto: UtenteDto) {
        reviewList.value = _reviewApi.findAll(utenteDto.id).toList()
        println(reviewList.value)
    }

    fun isSameUser(authViewModel: AuthViewModel, utenteDto: UtenteDto): Boolean {
        return authViewModel.loggedInUser.value!!.credenzialiEmail == utenteDto.credenzialiEmail
    }

    fun updateDescrizione(description: String) {
        val hasError = !RecensioneDto.validateDescrizione(description)
        _reviewState.value = _reviewState.value.copy(
            description = description,
            descriptionHasError = hasError
        )
    }

    fun updateRating(rating: Float) {
        _reviewState.value = _reviewState.value.copy(
            rating = rating
        )
    }

    fun addReview(
        authViewModel: AuthViewModel,
        descrizione: String,
        rating: Float,
        destinatario: UtenteDto
    ) {
        val recensioneDto = RecensioneDto(
            commento = descrizione, rating = rating,
            autoreCredenzialiEmail = authViewModel.loggedInUser.value!!.credenzialiEmail,
            destinatarioCredenzialiEmail = destinatario.credenzialiEmail
        )
        _reviewApi.add(recensioneDto)
    }

    fun deleteReview(
        id: Long
    ) {
        _reviewApi.delete(id)
    }
}