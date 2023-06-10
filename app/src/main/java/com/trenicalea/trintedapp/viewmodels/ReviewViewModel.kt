package com.trenicalea.trintedapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.RecensioneControllerApi
import com.trenicalea.trintedapp.models.Recensione
import com.trenicalea.trintedapp.models.RecensioneDto
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.models.UtenteRegistrationDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ReviewState(
    val description: String = "",
    var rating: Number = 0,
    val descriptionHasError: Boolean = !RecensioneDto.validateDescrizione(description)
)

class ReviewViewModel : ViewModel() {

    private val _reviewState = MutableStateFlow(ReviewState())
    val reviewState: StateFlow<ReviewState> = _reviewState.asStateFlow()


    private val _reviewApi: RecensioneControllerApi = RecensioneControllerApi()
    var reviewList: MutableState<List<Recensione>> = mutableStateOf(listOf())

    fun getUserReview(authViewModel: AuthViewModel){
        reviewList.value = _reviewApi.findAll(authViewModel.loggedInUser.value!!.id).toList()
    }

    fun isSameUser(authViewModel: AuthViewModel, utenteDto: UtenteDto) : Boolean{
        return authViewModel.loggedInUser.value!!.credenzialiEmail == utenteDto.credenzialiEmail
    }

    fun updateDescrizione(description: String) {
        val hasError = !RecensioneDto.validateDescrizione(description)
        _reviewState.value = _reviewState.value.copy(
            description = description,
            descriptionHasError = hasError
        )
    }

    fun updateRating(rating: Number) {
        _reviewState.value = _reviewState.value.copy(
            rating = rating
        )
    }

}