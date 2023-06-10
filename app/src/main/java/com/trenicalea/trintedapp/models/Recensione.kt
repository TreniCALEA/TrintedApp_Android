/**
 * OpenAPI definition
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package com.trenicalea.trintedapp.models


/**
 * 
 * @param id 
 * @param rating 
 * @param commento 
 * @param destinatario 
 * @param autore 
 * @param ordine 
 */
data class Recensione (

    val id: Long? = null,
    val rating: Float? = null,
    val commento: String? = null,
    val destinatario: Utente,
    val autore: Utente,
    val ordine: Ordine? = null
) {
}