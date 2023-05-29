package com.trenicalea.trintedapp.models
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
data class ArticoloDto (

    val id: Long,
    val titolo: String? = null,
    val descrizione: String? = null,
    val prezzo: Double,
    val immagini: Array<String>? = null,
    val utente: Long,
    val categoria: String? = null,
    val condizioni: String? = null
) {
}