package com.trenicalea.trintedapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trenicalea.trintedapp.models.ArticoloDto
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.viewmodels.ArticoloViewModel
import com.trenicalea.trintedapp.viewmodels.AuthViewModel
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticoloActivity(
    articoloViewModel: ArticoloViewModel,
    idProdotto: Long?,
    utente: MutableState<UtenteDto?>? = null,
    selectedIndex: MutableState<Int>? = null,
    isRedirected: MutableState<Boolean>? = null,
    utenteViewModel: UtenteViewModel,
    authViewModel: AuthViewModel
) {
    val articolo: ArticoloDto = articoloViewModel.getArticoloById(idProdotto!!)
    val articoloOwner: UtenteDto = utenteViewModel.getUser(articolo.utenteId)

    var showOrderForm = remember { mutableStateOf(false) }
    var showErrDialog = remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        val pageCount = articolo.immagini.size
        val pagerState = rememberPagerState()

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(5.dp)
        ) {
            // Row per le immagini
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                HorizontalPager(
                    pageCount = pageCount,
                    state = pagerState
                ) { page ->
                    // Our page content
                    Image(
                        bitmap = articolo.immagini[page].asImageBitmap(),
                        contentDescription = "Image $page of product: $articolo.titolo"
                    )
                }
            }

            // Divider
            Divider()

            // Button profile infos
            if (!articoloViewModel.openExternal.value) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        utente!!.value = articoloOwner
                        isRedirected!!.value = true
                        selectedIndex!!.value = 3
                    },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black.copy(alpha = 0.75f),
                        containerColor = Color.Transparent
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Account image",
                        modifier = Modifier.size(65.dp)
                    )
                    Column(
                        Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row {
                            Text(text = articoloOwner.credenzialiUsername)
                        }
                        Row(
                            modifier = Modifier.padding(top = 5.dp)
                        ) {
                            if (articoloOwner.ratingGenerale == null) {
                                Text(text = "")
                            }
                            Text(
                                text = "⭐ " + if (articoloOwner.ratingGenerale != null) "${articoloOwner.ratingGenerale}" else "Nessuna recensione"
                            )
                        }
                    }
                }
            }

            // Divider
            Divider()

            // Colonna prezzo
            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Prezzo: ${articolo.prezzo}€",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                // "Acquista" button
                Button(
                    onClick = {
                        if (!authViewModel.isLogged.value) {
                            showErrDialog.value = true
                        } else {
                            showOrderForm.value = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(150.dp),
                    shape = RectangleShape,

                    ) {
                    Text("Acquista!")
                }
            }

            // Descrizione
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            ) {
                Text(
                    text = "Descrizione",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = articolo.descrizione,
                    fontSize = 18.sp,
                )
            }

        }
    }

    if (showOrderForm.value) {
        OrderFormActivity(
            articolo,
            authViewModel,
            showOrderForm,
        ) {
            showOrderForm.value = false
        }
    }

    if (showErrDialog.value) {
        showAlert("Login richiesto", "Devi essere loggato per poter continuare!") {
            showErrDialog.value = false
            selectedIndex!!.value = 3
        }
    }
}
