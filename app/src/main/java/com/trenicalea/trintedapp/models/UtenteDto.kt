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
 * @param nome 
 * @param cognome
 * @param credenzialiEmail
 * @param isAdmin 
 * @param ratingGenerale 
 */
data class UtenteDto (

    val id: Long? = null,
    val nome: String,
    val cognome: String,
    val credenzialiEmail: String? = null,
    val isAdmin: Boolean? = null,
    val ratingGenerale: Float? = null
) {
}