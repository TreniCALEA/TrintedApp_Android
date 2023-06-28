package com.trenicalea.trintedapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.viewmodels.ArticoloViewModel
import com.trenicalea.trintedapp.viewmodels.AuthViewModel
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageActivity(
    articoloViewModel: ArticoloViewModel,
    utente: MutableState<UtenteDto?>,
    selectedIndex: MutableState<Int>,
    isRedirected: MutableState<Boolean>,
    utenteViewModel: UtenteViewModel,
    authViewModel: AuthViewModel,
    appwriteConfig: AppwriteConfig
) {
    articoloViewModel.getAllArticolo()
    val id: MutableState<Long?> = remember { mutableStateOf(null) }
    val openArticolo: MutableState<Boolean> = remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        if (articoloViewModel.articoloList.value.isEmpty()) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            ) {
                Text(text = "Nessun articolo presente.")
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            ) {
                Text(text = "Homepage", fontSize = 25.sp)
            }
            LazyColumn(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(articoloViewModel.articoloList.value) { articolo ->
                    key(articolo.id) {
                        if (articolo.acquistabile) {
                            Row {
                                Card(
                                    onClick = {
                                        id.value = articolo.id
                                        openArticolo.value = true
                                    },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {
                                    Column {
                                        Image(
                                            bitmap = articolo.immagini[0].asImageBitmap(),
                                            contentDescription = "Image of product: ${articolo.titolo}",
                                            contentScale = ContentScale.FillWidth,
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                        )
                                        Row(
                                            modifier = Modifier.padding(10.dp)
                                        ) {
                                            Column {
                                                Text(text = articolo.titolo)
                                            }
                                            Spacer(modifier = Modifier.weight(1f))
                                            Column {
                                                Text(text = "${articolo.prezzo}€")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (openArticolo.value) {
        ArticoloActivity(
            articoloViewModel,
            id.value,
            utente,
            selectedIndex,
            isRedirected,
            utenteViewModel,
            authViewModel,
            appwriteConfig
        )
    }
}