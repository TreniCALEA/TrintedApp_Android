package com.trenicalea.trintedapp


import ReviewActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.OrdineDto
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.viewmodels.AuthViewModel
import com.trenicalea.trintedapp.viewmodels.OrderViewModel
import com.trenicalea.trintedapp.viewmodels.ReviewViewModel
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel


@Composable
fun UserProfileActivity(
    user: UtenteDto,
    authViewModel: AuthViewModel,
    appwriteConfig: AppwriteConfig,
    utenteViewModel: UtenteViewModel,
    isRedirected: MutableState<Boolean>,
    selectedIndex: MutableState<Int>,
) {
    val orderViewModel = OrderViewModel()
    orderViewModel.getByAcquirente(user.id, appwriteConfig)
    orderViewModel.getByVenditore(user.id, appwriteConfig)

    var showReview by remember { mutableStateOf(false) }
    var showCompleteProfile by remember { mutableStateOf(false) }
    val eraseProfile = remember { mutableStateOf(false) }
    val makeAdmin = remember { mutableStateOf(false) }
    val revokeAdmin = remember { mutableStateOf(false) }

    val purchasesList = orderViewModel.ordersGetByAcquirente
    val salesList = orderViewModel.ordersGetByVenditore
    if (!showReview) {
        if (showCompleteProfile) CompleteProfile(
            appwriteConfig = appwriteConfig,
            authViewModel = authViewModel,
            utenteViewModel = utenteViewModel,
            selectedIndex = selectedIndex,
            onClose = { showCompleteProfile = false }
        )
        else {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (user.immagine != null) {
                            Image(
                                bitmap = user.immagine.asImageBitmap(),
                                contentDescription = "${stringResource(id = R.string.propic)} ${user.credenzialiUsername}"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = stringResource(id = R.string.defaultImage),
                                modifier = Modifier.size(60.dp)
                            )
                        }

                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = user.credenzialiUsername,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 25.sp)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { showReview = true }) {
                            Text(text = "Rating: " + (user.ratingGenerale ?: 0).toString())
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Default.Mail,
                            contentDescription = stringResource(id = R.string.profileEmailIcon)
                        )
                        Row {
                            Text(
                                text = user.credenzialiEmail,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    Divider()

                    if (isRedirected.value && authViewModel.loggedInUser.value!!.isAdmin!! && !user.isAdmin!!) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    utenteViewModel.makeAdmin(
                                        user.id, appwriteConfig
                                    )
                                    makeAdmin.value = true
                                }, colors = ButtonDefaults.buttonColors(Color.Green)
                            ) {
                                Text(text = "Rendi Admin!", color = Color.White)
                            }
                        }
                    }

                    if (isRedirected.value && authViewModel.loggedInUser.value!!.isAdmin!! && user.isAdmin!! && !user.isOwner!!) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Button(
                                onClick = {
                                    utenteViewModel.revokeAdmin(
                                        user.id, appwriteConfig
                                    )
                                    revokeAdmin.value = true
                                }, colors = ButtonDefaults.buttonColors(Color.Red)
                            ) {
                                Text(text = "Togli i diritti di Admin", color = Color.White)
                            }
                        }
                    }

                    if (!isRedirected.value) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Articoli venduti",
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            )
                        }
                        if (salesList.value.isNotEmpty()) Carousel(
                            list = salesList.value, title = stringResource(R.string.recentSales)
                        )
                        else ArrayEmpty()
                    }

                    Divider()

                    if (!isRedirected.value) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Articoli acquistati",
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            )
                        }
                        if (purchasesList.value.isNotEmpty()) Carousel(
                            list = purchasesList.value,
                            title = stringResource(R.string.recentPurchases)
                        )
                        else ArrayEmpty()
                    }
                }
                if (!isRedirected.value) {
                    if (!authViewModel.isVerified.value) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { authViewModel.verifyEmail(appwriteConfig) },
                                colors = ButtonDefaults.buttonColors(Color.Green),
                                modifier = Modifier.padding(start = 13.dp)
                            ) {
                                Text(text = "Verifica la tua e-mail!", color = Color.White)
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { showCompleteProfile = true },
                            modifier = Modifier.padding(start = 13.dp),
                            colors = ButtonDefaults.buttonColors(Color.Blue),
                        ) {
                            Text(text = "Completa o modifica il tuo profilo", color = Color.White)
                        }
                        Button(onClick = {
                            eraseProfile.value = true
                        }) {
                            Text(text = "Elimina il profilo")
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { authViewModel.logout(appwriteConfig) },
                            modifier = Modifier.width(150.dp),
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Text(text = "Logout", color = Color.White)
                        }
                    }
                }


            }
        }
        if (eraseProfile.value) {
            AlertDialog(onDismissRequest = { eraseProfile.value = false },
                title = { Text(text = "Sei sicuro?") },
                text = { Text(text = "Sei assolutamente certo di voler eliminare il profilo? Questa azione è irreversibile.") },
                confirmButton = {
                    Button(onClick = {
                        eraseProfile.value = false
                        utenteViewModel.deleteProfile(user.id, appwriteConfig)
                        selectedIndex.value = 3
                    }) {
                        Text(text = "Sì")
                    }
                },
                dismissButton = {
                    Button(onClick = { eraseProfile.value = false }) {
                        Text(text = "No")
                    }
                })
        }

        if (makeAdmin.value) {
            AlertDialog(onDismissRequest = { makeAdmin.value = false },
                title = { Text(text = "Sei sicuro?") },
                text = { Text(text = "Cliccando su 'Sì' si accetta che questo utente diventi un admin") },
                confirmButton = {
                    Button(onClick = {
                        makeAdmin.value = false
                        utenteViewModel.makeAdmin(user.id, appwriteConfig)
                        selectedIndex.value = 3

                    }) {
                        Text(text = "Sì")
                    }
                },
                dismissButton = {
                    Button(onClick = { eraseProfile.value = false }) {
                        Text(text = "No")
                    }
                })
        }

        if (revokeAdmin.value) {
            AlertDialog(onDismissRequest = { makeAdmin.value = false },
                title = { Text(text = "Sei sicuro?") },
                text = { Text(text = "Cliccando su 'Sì' si accetta che a questo utente vengano tolti i diritti di admin") },
                confirmButton = {
                    Button(onClick = {
                        revokeAdmin.value = false
                        utenteViewModel.revokeAdmin(user.id, appwriteConfig)
                        selectedIndex.value = 3
                    }) {
                        Text(text = "Sì")
                    }
                },
                dismissButton = {
                    Button(onClick = { eraseProfile.value = false }) {
                        Text(text = "No")
                    }
                })
        }

    } else ReviewActivity(
        appwriteConfig = appwriteConfig,
        authViewModel = authViewModel,
        reviewViewModel = ReviewViewModel(),
        utenteDto = user
    )
}

@Composable
fun ArrayEmpty() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.ProductionQuantityLimits,
            contentDescription = stringResource(id = R.string.noItems),
            modifier = Modifier.size(40.dp)
        )
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = stringResource(id = R.string.noItems), fontSize = 20.sp)
    }
}

@Composable
fun Carousel(list: Array<OrdineDto>, title: String) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp
        )
    }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        LazyRow {
            items(list) { order ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentHeight()
                        .wrapContentWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(text = "${order.dataAcquisto}")
                        }
                    }

                }
            }
        }
    }
}


