package com.trenicalea.trintedapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Streetview
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.ArticoloDto
import com.trenicalea.trintedapp.models.Indirizzo
import com.trenicalea.trintedapp.viewmodels.AuthViewModel
import com.trenicalea.trintedapp.viewmodels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderFormActivity(
    appwriteConfig: AppwriteConfig,
    articoloDto: ArticoloDto,
    authViewModel: AuthViewModel,
    showForm: MutableState<Boolean>? = null,
    onDismissRequest: () -> Unit
) {
    val orderViewModel = OrderViewModel()
    val sheetState = rememberModalBottomSheetState()

    val orderState by orderViewModel.ordineInfosState.collectAsState()

    val acquirente = authViewModel.loggedInUser.value!!
    val usePreExistentAddress = remember { mutableStateOf(false) }
    val showCompleteDialog = remember { mutableStateOf(false) }

    // Modal bottom sheet
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        // Main column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Checkbox for 'usePreExistentAddress' boolean value
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = usePreExistentAddress.value,
                    onCheckedChange = { usePreExistentAddress.value = it },
                )
                Text(text = "Usa l'indirizzo esistente")
            }

            // Via
            OutlinedTextField(
                value = orderState.via,
                onValueChange = { orderViewModel.updateVia(it) },
                label = { Text(text = "Via") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Streetview,
                        contentDescription = "Street"
                    )
                },
            )

            // Civico
            OutlinedTextField(
                value = orderState.civico,
                onValueChange = { orderViewModel.updateCivico(it) },
                label = { Text(text = "Civico") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Numbers,
                        contentDescription = "Street number"
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Paese
            OutlinedTextField(
                value = orderState.paese,
                onValueChange = { orderViewModel.updatePaese(it) },
                label = { Text(text = "Città") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocationCity,
                        contentDescription = "City"
                    )
                },
            )

            // Order button
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    val indirizzo =
                        Indirizzo(orderState.via, orderState.civico.toInt(), orderState.paese)
                    orderViewModel.confirmOrder(acquirente.id, articoloDto.id!!, indirizzo, appwriteConfig)

                    showCompleteDialog.value = true
                },
            ) {
                Text(text = "Effettua l'ordine!")
            }
        }
    }

    if (usePreExistentAddress.value) {
        if (acquirente.indirizzo != null) {
            orderViewModel.updateVia(acquirente.indirizzo.via!!)
            orderViewModel.updateCivico(acquirente.indirizzo.numeroCivico!!.toString())
            orderViewModel.updatePaese(acquirente.indirizzo.citta!!)
        } else {
            showAlert("Attenzione", "Nessun indirizzo salvato!") {
                usePreExistentAddress.value = false
            }
        }
    } else {
        orderViewModel.updateVia("")
        orderViewModel.updateCivico("")
        orderViewModel.updatePaese("")
    }

    if (showCompleteDialog.value) {
        showAlert("Evviva!", "Hai acquistato quest'oggetto!") {
            showCompleteDialog.value = false

            showForm!!.value = false
        }
    }
}