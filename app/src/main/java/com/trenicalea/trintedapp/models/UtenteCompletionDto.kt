package com.trenicalea.trintedapp.models

import android.graphics.Bitmap
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UtenteCompletionDto(
    val nome: String? = null,
    val cognome: String? = null,
    @Json(name = "immagine")
    val immagine: Bitmap? = null,
    val indirizzo: Indirizzo? = null
) {
}