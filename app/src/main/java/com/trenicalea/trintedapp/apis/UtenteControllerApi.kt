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

import com.trenicalea.trintedapp.infrastructure.*
import com.trenicalea.trintedapp.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class UtenteControllerApi(basePath: kotlin.String = "http://localhost:8080") : ApiClient(basePath) {

    fun add(body: UtenteRegistrationDto): UtenteRegistrationDto = runBlocking(Dispatchers.IO) {
        val localVariableBody: Any = body
        val localVariableConfig = RequestConfig(
                RequestMethod.POST,
                "/user-api/users"
        )
        val response = request<UtenteRegistrationDto>(
                localVariableConfig, localVariableBody
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as UtenteRegistrationDto
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     *
     *
     * @return kotlin.Array<UtenteBasicDto>
     */
    @Suppress("UNCHECKED_CAST")
    fun all(): Array<UtenteBasicDto> = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/user-api/users"
        )
        val response = request<Array<UtenteBasicDto>>(
                localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Array<UtenteBasicDto>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     *
     *
     * @param idUser
     * @return kotlin.String
     */
    @Suppress("UNCHECKED_CAST")
    fun delete(idUser: Long): String = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
                RequestMethod.DELETE,
                "/user-api/users/{idUser}".replace("{" + "idUser" + "}", "$idUser")
        )
        val response = request<String>(
                localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as String
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     *
     *
     * @param prefix
     * @param page
     * @return PageUtenteBasicDto
     */
    @Suppress("UNCHECKED_CAST")
    fun getAllByUsernameLikePaged(prefix: String, page: Int): PageUtenteBasicDto = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/user-api/users/{prefix}/{page}".replace("{" + "prefix" + "}", "$prefix").replace("{" + "page" + "}", "$page")
        )
        val response = request<PageUtenteBasicDto>(
                localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as PageUtenteBasicDto
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     *
     *
     * @param page
     * @return PageUtenteBasicDto
     */
    @Suppress("UNCHECKED_CAST")
    fun getAllPaged(page: Int): PageUtenteBasicDto = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/user-api/users/all/{page}".replace("{" + "page" + "}", "$page")
        )
        val response = request<PageUtenteBasicDto>(
                localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as PageUtenteBasicDto
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     *
     *
     * @param idUser
     * @return UtenteDto
     */
    @Suppress("UNCHECKED_CAST")
    fun getById(idUser: Long): UtenteDto = runBlocking(Dispatchers.IO) {
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/user-api/users/{idUser}".replace("{" + "idUser" + "}", "$idUser")
        )
        val response = request<UtenteDto>(
                localVariableConfig
        )

        return@runBlocking when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as UtenteDto
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
}
