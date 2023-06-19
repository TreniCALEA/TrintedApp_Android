package com.trenicalea.trintedapp.models

import android.graphics.Bitmap
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 *
 * @param id
 * @param titolo
 * @param descrizione
 * @param prezzo
 * @param immagini
 * @param utente
 * @param categoria
 * @param condizioni
 */
@JsonClass(generateAdapter = true)
data class ArticoloDto(

    val id: Long? = null,
    val titolo: String,
    val descrizione: String,
    val prezzo: Double,
    @Json(name = "immagini")
    val immagini: List<Bitmap>,
    val utenteId: Long,
    val categoria: String? = null,
    val condizioni: String? = null,
    val acquistabile: Boolean
) {
    companion object {
        fun validateTitolo(titolo: String): Boolean {
            return titolo.isNotEmpty()
        }

        fun validateDescrizione(descrizione: String): Boolean {
            return descrizione.isNotEmpty()
        }

        fun validatePrezzo(prezzo: Double): Boolean {
            return prezzo.isFinite().and(prezzo > 0.0)
        }
    }
}