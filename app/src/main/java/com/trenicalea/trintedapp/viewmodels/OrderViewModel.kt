package com.trenicalea.trintedapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.trenicalea.trintedapp.apis.OrdineControllerApi
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.Indirizzo
import com.trenicalea.trintedapp.models.OrdineDto
import io.appwrite.Client
import io.appwrite.services.Account
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Base64

data class OrdineInfos(
    val via: String = "",
    val civico: String = "",
    val paese: String = "",
    val viaHasError: Boolean = !OrdineDto.validateVia(via),
    val civicoHasError: Boolean = !OrdineDto.validateCivico(civico),
    val paeseHasError: Boolean = !OrdineDto.validatePaese(paese),
)

class OrderViewModel : ViewModel() {
    private val _ordineInfosState = MutableStateFlow(OrdineInfos())
    val ordineInfosState: StateFlow<OrdineInfos> = _ordineInfosState.asStateFlow()

    private val _orderApi: OrdineControllerApi = OrdineControllerApi()
    var ordersGetByVenditore: MutableState<Array<OrdineDto>> = mutableStateOf(arrayOf())
    var ordersGetByAcquirente: MutableState<Array<OrdineDto>> = mutableStateOf(arrayOf())
    var loading: MutableState<Boolean> = mutableStateOf(false)

    fun getByAcquirente(id: Long, appwrite: AppwriteConfig) {
        genericGetBy(id, appwrite, true)
    }

    fun getByVenditore(id: Long, appwrite: AppwriteConfig) {
        genericGetBy(id, appwrite, false)
    }

    fun genericGetBy(id: Long, appwrite: AppwriteConfig, acquirenteOrVenditore: Boolean) {
        loading.value = true
        val client: Client = Client(appwrite.appContext)
            .setEndpoint(appwrite.endpoint)
            .setProject(appwrite.projectId)

        val account = Account(client)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val encodedString =
                    Base64.getEncoder().encodeToString(account.createJWT().jwt.toByteArray())
                if (acquirenteOrVenditore && ordersGetByAcquirente.value.isEmpty()) {
                    ordersGetByAcquirente.value = _orderApi.getByAcquirente(id, encodedString)
                    println("OrdersGetByAcquirente size: " + ordersGetByAcquirente.value.size)
                } else if (!acquirenteOrVenditore && ordersGetByVenditore.value.isEmpty()) {
                    ordersGetByVenditore.value = _orderApi.getByVenditore(id, encodedString)
                    println("OrdersGetByVenditore size: " + ordersGetByVenditore.value.size)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("[D] Unable to retrieve " + if (acquirenteOrVenditore) "bought items" else "sold items")
            }
        }.invokeOnCompletion {
            loading.value = false
        }
    }

    fun confirmOrder(
        acquirente: Long,
        articoloId: Long,
        indirizzo: Indirizzo,
        appwriteConfig: AppwriteConfig
    ) {
        val client: Client = Client(appwriteConfig.appContext)
            .setEndpoint(appwriteConfig.endpoint)
            .setProject(appwriteConfig.projectId)

        val account = Account(client)
        CoroutineScope(Dispatchers.IO).launch {
            val encodedString: String =
                Base64.getEncoder().encodeToString(account.createJWT().jwt.toByteArray())
            _orderApi.add(acquirente, articoloId, indirizzo, encodedString)
        }
    }

    fun getOrder(id: Long): OrdineDto {
        return _orderApi.getById(id)
    }

    fun updateVia(via: String) {
        val hasError = !OrdineDto.validateVia(via)
        _ordineInfosState.value = _ordineInfosState.value.copy(
            via = via,
            viaHasError = hasError
        )
    }

    fun updateCivico(civico: String) {
        val hasError = !OrdineDto.validateCivico(civico)
        _ordineInfosState.value = _ordineInfosState.value.copy(
            civico = civico,
            civicoHasError = hasError
        )
    }

    fun updatePaese(paese: String) {
        val hasError = !OrdineDto.validatePaese(paese)
        _ordineInfosState.value = _ordineInfosState.value.copy(
            paese = paese,
            paeseHasError = hasError
        )
    }
}