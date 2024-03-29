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
 * @param pageNumber
 * @param pageSize
 * @param paged
 * @param unpaged
 * @param sort
 * @param offset
 */
data class PageableObject(

    val pageNumber: Int? = null,
    val pageSize: Int? = null,
    val paged: Boolean? = null,
    val unpaged: Boolean? = null,
    val sort: SortObject? = null,
    val offset: Long? = null
) {
}