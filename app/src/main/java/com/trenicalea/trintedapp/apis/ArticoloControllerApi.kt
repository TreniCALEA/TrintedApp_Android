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
package com.trenicalea.trintedapp.apis

import com.trenicalea.trintedapp.Config
import com.trenicalea.trintedapp.infrastructure.*
import com.trenicalea.trintedapp.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ArticoloControllerApi(basePath: String = Config.ip) : ApiClient(basePath) {

    /**
     *
     *
     * @param body
     * @return ArticoloDto
     */
    fun add(body: ArticoloDto, param: String): ArticoloDto = runBlocking(Dispatchers.IO) {
        val localVariableBody: Any = body
        val localVariableConfig = RequestConfig(
            RequestMethod.POST,
            "/item-api/item",
            query = mapOf(Pair("jwt", listOf(param)))
        )
        val response = request<ArticoloDto>(
            localVariableConfig, localVariableBody
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as ArticoloDto
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )

            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     *
     *
     * @return kotlin.Array<ArticoloDto>
     */
    @Suppress("UNCHECKED_CAST")
    fun all(): Array<ArticoloDto> = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/item-api/item"
        )
        val response = request<Array<ArticoloDto>>(
            localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<ArticoloDto>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )

            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     *
     *
     * @param idItem
     */
    fun delete(idItem: Long, param: String): String = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
            RequestMethod.DELETE,
            "/item-api/item/$idItem",
            query = mapOf(Pair("jwt", listOf(param)))
        )
        val response = request<String>(
            localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as String
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )

            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    /**
     *
     *
     * @param idItem
     * @return ArticoloDto
     */
    fun getById(idItem: Long): ArticoloDto = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/item-api/item/$idItem"
        )
        val response = request<ArticoloDto>(
            localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as ArticoloDto
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException(
                (response as ClientError<*>).body as? String ?: "Client error"
            )

            ResponseType.ServerError -> throw ServerException(
                (response as ServerError<*>).message ?: "Server error"
            )
        }
    }

    fun getByTitoloContainingOrDescrizioneContaining(searchValue: String): Array<ArticoloDto> =
        runBlocking(Dispatchers.IO) {
            val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/item-api/item/search/$searchValue"
            )
            val response = request<Array<ArticoloDto>>(
                localVariableConfig
            )

            return@runBlocking when (response.responseType) {
                ResponseType.Success -> (response as Success<*>).data as Array<ArticoloDto>
                ResponseType.Informational -> TODO()
                ResponseType.Redirection -> TODO()
                ResponseType.ClientError -> throw ClientException(
                    (response as ClientError<*>).body as? String ?: "Client error"
                )

                ResponseType.ServerError -> throw ServerException(
                    (response as ServerError<*>).message ?: "Server error"
                )
            }
        }
}
