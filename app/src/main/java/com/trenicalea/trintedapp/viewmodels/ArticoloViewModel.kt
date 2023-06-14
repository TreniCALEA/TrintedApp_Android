package com.trenicalea.trintedapp.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.ArticoloControllerApi
import com.trenicalea.trintedapp.models.ArticoloDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.FileNotFoundException

data class AddArticoloState(
    val titolo: String = "",
    val descrizione: String = "",
    val prezzo: Double = 0.0,
    val immagini: List<Bitmap> = listOf(),
    val categoria: String? = "",
    val condizioni: String? = "",
    val titoloHasError: Boolean = !ArticoloDto.validateTitolo(titolo),
    val descrizioneHasError: Boolean = !ArticoloDto.validateDescrizione(descrizione),
    val prezzoHasError: Boolean = !ArticoloDto.validatePrezzo(prezzo)
)

class ArticoloViewModel : ViewModel() {
    private val _addArticoloState = MutableStateFlow(AddArticoloState())
    val addArticoloState: StateFlow<AddArticoloState> = _addArticoloState.asStateFlow()

    private val _articoloApi: ArticoloControllerApi = ArticoloControllerApi()
    var articoloList: MutableState<Array<ArticoloDto>> = mutableStateOf(arrayOf())
    var prefix: MutableState<String> = mutableStateOf("")
    var searchArticolo: MutableState<List<ArticoloDto>> = mutableStateOf(listOf())
    val openExternal: MutableState<Boolean> = mutableStateOf(false)
    var openIndirizzo: MutableState<Boolean> = mutableStateOf(false)

    fun addArticolo(
        authViewModel: AuthViewModel,
        titolo: String,
        descrizione: String,
        prezzo: Double,
        immagini: List<Bitmap>,
        categoria: String? = null,
        condizioni: String? = null,
    ) {

        println(authViewModel.loggedInUser.value)
        val articoloDto = ArticoloDto(
            titolo = titolo,
            descrizione = descrizione,
            prezzo = prezzo,
            immagini = immagini,
            utenteId = authViewModel.loggedInUser.value!!.id,
            categoria = categoria,
            condizioni = condizioni,
        )
        _articoloApi.add3(articoloDto)
    }

    fun getArticoloById(id: Long): ArticoloDto {
        return _articoloApi.getById2(id)
    }

    fun deleteArticoloById(id: Long) {
        _articoloApi.delete3(id)
    }

    fun searchArticolo() {
        searchArticolo.value =
            _articoloApi.getByTitoloContainingOrDescrizioneContaining(prefix.value).toList()
    }

    fun getAllArticolo() {
        try {
            articoloList.value = _articoloApi.all2()
        } catch (e: Exception) {
            e.printStackTrace()
            articoloList.value = arrayOf()
        }
    }

    fun updateTitolo(titolo: String) {
        val hasError = !ArticoloDto.validateTitolo(titolo)
        _addArticoloState.value = _addArticoloState.value.copy(
            titolo = titolo,
            titoloHasError = hasError
        )
    }

    fun updateDescrizione(descrizione: String) {
        val hasError = !ArticoloDto.validateDescrizione(descrizione)
        _addArticoloState.value = _addArticoloState.value.copy(
            descrizione = descrizione,
            descrizioneHasError = hasError
        )
    }

    fun updatePrezzo(prezzo: Double) {
        val hasError = !ArticoloDto.validatePrezzo(prezzo)
        _addArticoloState.value = _addArticoloState.value.copy(
            prezzo = prezzo,
            prezzoHasError = hasError
        )
    }

    @Composable
    fun decodeUriAsBitmap(uri: Uri?): Bitmap? {
        val context: Context = LocalContext.current
        var bitmap: Bitmap? = null
        bitmap = try {
            BitmapFactory.decodeStream(
                context
                    .contentResolver.openInputStream(uri!!)
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
        return bitmap
    }

    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun updateImmagini(uris: List<Uri>) {
        val bitmaps = mutableListOf<Bitmap>()

        for (uri in uris) {
            val bitmap = decodeUriAsBitmap(uri)
            if (bitmap != null) {
                bitmaps.add(bitmap)
            }
        }

        _addArticoloState.value = _addArticoloState.value.copy(
            immagini = bitmaps
        )
    }
}