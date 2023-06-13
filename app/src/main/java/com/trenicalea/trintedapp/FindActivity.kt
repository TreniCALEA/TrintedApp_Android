package com.trenicalea.trintedapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trenicalea.trintedapp.models.UtenteBasicDto
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel

@Composable
fun FindActivity(
    userViewModel: UtenteViewModel,
    selectedIndex: MutableState<Int>,
    isRedirected: MutableState<Boolean>,
    selectedHomePageIndex: MutableState<Int>,
    user: MutableState<UtenteDto?>
) {
    var searchValue by remember { mutableStateOf("") }

    Column {
        Row {
            NavigationBar {
                NavigationBarItem(selected = selectedIndex.value == 0,
                    onClick = { selectedIndex.value = 0 },
                    icon = {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = stringResource(R.string.utenti)
                            )
                            Text(text = "Utenti")
                        }
                    })
                NavigationBarItem(selected = selectedIndex.value == 1,
                    onClick = { selectedIndex.value = 1 },
                    icon = {
                        Row {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = stringResource(R.string.articoli)
                            )
                            Text(text = "Articoli")
                        }
                    })
            }
        }
        if (selectedIndex.value == 0) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .width(500.dp)
            ) {
                OutlinedTextField(value = searchValue, onValueChange = {
                    searchValue = it
                    userViewModel.prefix.value = searchValue
                }, isError = searchValue == "", leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                }, label = { Text(stringResource(R.string.cercaUtenti)) })
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(onClick = {
                    if (searchValue != "") userViewModel.getUserByUsernameLike()
                }, modifier = Modifier.padding(horizontal = 150.dp)) {
                    Text(text = stringResource(R.string.search))
                }
            }
            ListUsers(
                userList = userViewModel.userList.value,
                redirect = isRedirected,
                utente = user,
                selectedIndex = selectedHomePageIndex,
                utenteViewModel = userViewModel
            )

        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .width(500.dp)
            ) {
                OutlinedTextField(value = searchValue, onValueChange = {
                    searchValue = it
                    // Da cambiare con la ricerca degli articoli
                    // userViewModel.prefix.value = searchValue
                }, isError = searchValue == "", leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                }, label = { Text(stringResource(R.string.cercaArticoli)) })

            }
            Button(
                onClick = {
                    if (searchValue != "") userViewModel.getUserByUsernameLike()
                }, modifier = Modifier.padding(horizontal = 150.dp)
            ) {
                Text(text = stringResource(R.string.search))
            }
        }
    }
}

@Composable
fun ListUsers(
    userList: List<UtenteBasicDto>,
    redirect: MutableState<Boolean>,
    utente: MutableState<UtenteDto?>,
    selectedIndex: MutableState<Int>,
    utenteViewModel: UtenteViewModel
) {
    if (userList.isEmpty()) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Nessun utente")
        }
    } else {
        LazyColumn {
            items(userList) { user ->
                key(user.id) {
                    OutlinedButton(
                        onClick = {
                            redirect.value = true
                            utente.value = utenteViewModel.getUser(user.id)
                            selectedIndex.value = 3
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black.copy(0.20f),
                            containerColor = Color.Transparent
                        )
                    ) {
                        if (user.image != null) {
                            Image(
                                bitmap = user.image.asImageBitmap(),
                                contentDescription = "${stringResource(id = R.string.propic)} ${user.credenzialiUsername}"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = stringResource(id = R.string.defaultImage),
                                modifier = Modifier.size(60.dp),
                                tint = Color.Black

                            )
                        }
                        Text(
                            text = user.credenzialiUsername,
                            modifier = Modifier.weight(1f),
                            color = Color.Black
                        )
                        Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                            if (user.ratingGenerale != null) {
                                Text(
                                    text = user.ratingGenerale.toString(),
                                    color = Color.Black
                                )
                                for (i in 1..user.ratingGenerale.toInt()) {
                                    Text(
                                        text = "⭐",
                                        color = Color.Black
                                    )
                                }
                            } else {
                                Text(
                                    text = "Nessuna recensione"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
