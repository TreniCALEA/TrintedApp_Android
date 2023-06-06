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

import android.graphics.Bitmap
import com.squareup.moshi.Json
import com.trenicalea.trintedapp.models.BitmapAdapter

data class UtenteBasicDto (

    val id: Long,
    @Json(name = "immagine")
    val immagine: Bitmap? = null,
    val credenzialiUsername: String,
    val ratingGenerale: Float? = null
) {
}