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


class OrdineControllerApi(basePath: String = Config.ip) : ApiClient(basePath) {

    /**
     *
     *
     * @param acquirente
     * @param articoloDto
     * @param indirizzo
     * @return OrdineDto
     */
    fun add(acquirente: Long, articoloId: Long, body: Indirizzo, param: String): String =
        runBlocking(Dispatchers.IO) {
            val localVariableBody: Any = body
            val localVariableConfig = RequestConfig(
                RequestMethod.POST,
                "/order-api/orders/$acquirente/$articoloId",
                query = mapOf(Pair("jwt", listOf(param)))
            )
            val response = request<String>(
                localVariableConfig, localVariableBody
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
     * @return kotlin.Array<OrdineDto>
     */
    @Suppress("UNCHECKED_CAST")
    fun all(): Array<OrdineDto> = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/order-api/orders"
        )
        val response = request<Array<OrdineDto>>(
            localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<OrdineDto>
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
     * @param orderId
     * @return kotlin.String
     */
    @Suppress("UNCHECKED_CAST")
    fun delete(orderId: Long): String = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
            RequestMethod.DELETE,
            "/order-api/orders/$orderId"
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
     * @param id
     * @return kotlin.Array<OrdineDto>
     */
    @Suppress("UNCHECKED_CAST")
    fun getByAcquirente(id: Long): Array<OrdineDto> = runBlocking(Dispatchers.IO) {
        val localVariableQuery: MultiValueMap =
            mutableMapOf<String, List<String>>().apply {
                put("id", listOf(id.toString()))
            }
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/order-api/buyer", query = localVariableQuery
        )
        val response = request<Array<OrdineDto>>(
            localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<OrdineDto>
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
     * @param orderId
     * @return OrdineDto
     */
    @Suppress("UNCHECKED_CAST")
    fun getById(orderId: Long): OrdineDto = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/order-api/orders/$orderId"
        )
        val response = request<OrdineDto>(
            localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as OrdineDto
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
     * @param id
     * @return kotlin.Array<OrdineDto>
     */
    @Suppress("UNCHECKED_CAST")
    fun getByVenditore(id: Long): Array<OrdineDto> = runBlocking(Dispatchers.IO) {
        val localVariableQuery: MultiValueMap =
            mutableMapOf<String, List<String>>().apply {
                put("id", listOf(id.toString()))
            }
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/order-api/seller", query = localVariableQuery
        )
        val response = request<Array<OrdineDto>>(
            localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<OrdineDto>
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
