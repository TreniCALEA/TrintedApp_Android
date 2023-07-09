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

class UtenteRegistrationDto(
    val credenzialiUsername: String,
    val credenzialiEmail: String,
    val credenzialiPassword: String
) {
    companion object {
        fun validateCredenzialiUsername(username: String): Boolean {
            return username.isNotEmpty()
        }

        fun validateCredenzialiEmail(email: String): Boolean {
            return email.isNotEmpty() && email.contains("@") && email.contains(".")
        }

        fun validateCredenzialiPassword(password: String): Boolean {
            return password.isNotEmpty() && password.length > 8
        }
    }
}