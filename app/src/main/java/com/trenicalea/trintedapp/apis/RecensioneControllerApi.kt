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

class RecensioneControllerApi(basePath: String = Config.ip) : ApiClient(basePath) {

    /**
     *
     *
     * @param body
     * @return kotlin.String
     */
    @Suppress("UNCHECKED_CAST")
    fun add(body: RecensioneDto, param: String): String = runBlocking(Dispatchers.IO) {
        val localVariableBody: Any = body
        val localVariableConfig = RequestConfig(
            RequestMethod.POST,
            "/review-api/review",
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
     * @param reviewId
     * @return kotlin.String
     */
    @Suppress("UNCHECKED_CAST")
    fun delete(reviewId: Long, param: String): String = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
            RequestMethod.DELETE,
            "/review-api/review/$reviewId",
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
     * @param reviewId
     * @return kotlin.Array<Recensione>
     */
    @Suppress("UNCHECKED_CAST")
    fun findAll(userId: Long): Array<Recensione> = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/review-api/review/$userId"
        )
        val response = request<Array<Recensione>>(
            localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<Recensione>
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
     * @param review
     * @return PageRecensioneDto
     */
    @Suppress("UNCHECKED_CAST")
    fun getAllPaged(review: Int): PageRecensioneDto = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/review-api/review/all/$review"
        )
        val response = request<PageRecensioneDto>(
            localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as PageRecensioneDto
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
