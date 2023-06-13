package com.trenicalea.trintedapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import com.trenicalea.trintedapp.viewmodels.ArticoloViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageActivity(
    articoloViewModel: ArticoloViewModel,
) {
    articoloViewModel.getAllArticolo()
    val id: MutableState<Long?> = remember { mutableStateOf(null) }
    val openArticolo: MutableState<Boolean> = remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), modifier = Modifier
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
            LazyColumn {
                items(articoloViewModel.articoloList.value) { articolo ->
                    key(articolo.id) {
                        Row {
                            Card(
                                onClick = {
                                    id.value = articolo.id
                                    openArticolo.value = true
                                }
                            ) {
                                // Immagine
                                Image(
                                    bitmap = articolo.immagini[0].asImageBitmap(),
                                    contentDescription = "Image of product: $articolo.titolo"
                                )
                                Row {
                                    Column {
                                        Text(text = articolo.titolo)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Column {
                                        Text(text = "${articolo.prezzo}€")
                                    }
                                    Text(text = articolo.titolo)
                                }


                            }
                        }
                    }
                }
            }
        }
    }

    if (openArticolo.value) {
        ArticoloActivity(articoloViewModel, id.value)
    }
}