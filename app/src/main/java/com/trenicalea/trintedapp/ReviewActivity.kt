package com.trenicalea.trintedapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.Recensione
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.viewmodels.AuthViewModel
import com.trenicalea.trintedapp.viewmodels.ReviewViewModel

@Composable
fun ReviewActivity(
    appwriteConfig: AppwriteConfig,
    reviewViewModel: ReviewViewModel,
    utenteDto: UtenteDto,
    authViewModel: AuthViewModel
) {
    val reviewList: List<Recensione> = reviewViewModel.reviewList.value
    val reviewState by reviewViewModel.reviewState.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (reviewList.isEmpty()) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Non è presente alcuna recensione")
            }
        } else {
            LazyColumn {
                items(reviewList) { review ->
                    key(review.id) {
                        Row {
                            if (review.destinatario.immagine != null) {
                                Image(
                                    bitmap = review.destinatario.immagine.asImageBitmap(),
                                    contentDescription = "${stringResource(id = R.string.propic)} ${review.autore}"
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = stringResource(id = R.string.defaultImage),
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                            Text(
                                text = review.autore.credenziali.username,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = stringResource(id = R.string.rating)
                            )
                            Text(text = review.rating.toString())
                        }
                        Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                            Text(text = review.commento.toString())
                        }
                        Divider()
                    }
                }
            }
        }
        if (!reviewViewModel.isSameUser(authViewModel, utenteDto)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Rating: ")
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    for (i in 1..5) {
                        DropdownMenuItem(
                            text = { Text(text = "$i") },
                            onClick = {
                                println("Nuovo rating: $i")
                                reviewViewModel.updateRating(i)
                                expanded = false
                            }
                        )
                    }
                }
                OutlinedTextField(
                    isError = reviewState.descriptionHasError,
                    value = reviewState.description,
                    onValueChange = { reviewViewModel.updateDescrizione(it) },
                    label = { Text("Lascia una recensione qui...") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {

                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = "Invia")
                }
            }
        }
    }
}