package com.trenicalea.trintedapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.viewmodels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowOrdersActivity(
    showAcquisti: MutableState<Boolean>,
    userId: Long,
    appwriteConfig: AppwriteConfig,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val orderViewModel = remember { OrderViewModel() }

    LaunchedEffect(Unit) {
        if (showAcquisti.value) {
            orderViewModel.getByAcquirente(userId, appwriteConfig)
            println("Prendo le robe come acquirente")
        } else {
            orderViewModel.getByVenditore(userId, appwriteConfig)
            println("Prendo le robe come venditore")
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                if (!orderViewModel.loading.value)
                    Text(if (if (showAcquisti.value) orderViewModel.ordersGetByAcquirente.value.isEmpty() else orderViewModel.ordersGetByVenditore.value.isEmpty()) "Nessun ordine presente." else "Lista ordini")
                else
                    Text("Loading...")
            }
        }
        if (if (showAcquisti.value) orderViewModel.ordersGetByAcquirente.value.isNotEmpty() else orderViewModel.ordersGetByVenditore.value.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(if (showAcquisti.value) orderViewModel.ordersGetByAcquirente.value else orderViewModel.ordersGetByVenditore.value) { ordine ->
                    key(ordine.id) {
                        Divider()
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text("ID ordine: " + ordine.id)
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text("Data di acquisto: " + ordine.dataAcquisto)
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                "Via: " + ordine.indirizzo!!.via + " " +
                                        ordine.indirizzo.numeroCivico + ", " +
                                        ordine.indirizzo.citta
                            )
                        }
                    }
                }
            }
        }
    }
}