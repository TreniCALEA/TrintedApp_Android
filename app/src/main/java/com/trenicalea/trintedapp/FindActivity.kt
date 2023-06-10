package com.trenicalea.trintedapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trenicalea.trintedapp.models.UtenteBasicDto
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel

@Composable
fun FindActivity(
    userViewModel: UtenteViewModel, selectedIndex: MutableState<Int>
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
            ListUsers(userList = userViewModel.userList.value)

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
fun ListUsers(userList: List<UtenteBasicDto>) {
    if (userList.isEmpty()) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "Nessun utente")
        }
    } else {
        LazyColumn {
            items(userList) { user ->
                key(user.id) {
                    Row {
                        if (user.image != null) {
                            Image(
                                bitmap = user.image.asImageBitmap(),
                                contentDescription = "${stringResource(id = R.string.propic)} ${user.credenzialiUsername}"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = stringResource(id = R.string.defaultImage),
                                modifier = Modifier.size(60.dp)
                            )
                        }
                        Text(text = user.credenzialiUsername, modifier = Modifier.weight(1f))
                    }
                    Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = stringResource(id = R.string.rating)
                        )
                        Text(text = if (user.ratingGenerale == 0.0f) "Nessuna recensione" else user.ratingGenerale.toString())
                    }
                    Divider()
                }
            }
        }
    }
}
