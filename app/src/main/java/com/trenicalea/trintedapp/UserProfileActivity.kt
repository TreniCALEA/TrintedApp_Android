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
import androidx.compose.material3.Button
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
import com.trenicalea.trintedapp.viewmodels.OrdineListsViewModel
import com.trenicalea.trintedapp.viewmodels.ReviewViewModel
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel


@Composable
fun UserProfileActivity(
    user: UtenteDto,
    authViewModel: AuthViewModel,
    appwriteConfig: AppwriteConfig,
    utenteViewModel: UtenteViewModel,
    isRedirected: MutableState<Boolean>,
    selectedIndex: MutableState<Int>
) {
    val ordineListsViewModel = OrdineListsViewModel(user.id)
    var showReview by remember { mutableStateOf(false) }
    var showCompleteProfile by remember { mutableStateOf(false) }
    val purchasesList = ordineListsViewModel.ordersGetByAcquirente
    val salesList = ordineListsViewModel.ordersGetByVenditore
    if (!showReview) {
        if (showCompleteProfile) CompleteProfile(
            authViewModel = authViewModel,
            utenteViewModel = utenteViewModel,
            selectedIndex = selectedIndex
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
                        Button(
                            onClick = { showReview = true }
                        ) {
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
                        if (salesList.isNotEmpty()) Carousel(
                            list = salesList, title = stringResource(R.string.recentSales)
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
                        if (purchasesList.isNotEmpty()) Carousel(
                            list = purchasesList, title = stringResource(R.string.recentPurchases)
                        )
                        else ArrayEmpty()
                    }
                }
                if (!isRedirected.value) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { showCompleteProfile = true },
                            modifier = Modifier.padding(start = 13.dp)
                        ) {
                            Text(text = "Completa profilo")
                        }
                        Button(
                            onClick = {
                                utenteViewModel.deleteProfile(authViewModel.loggedInUser.value!!.id)
                                authViewModel.logout(appwriteConfig)
                                selectedIndex.value = 3
                            }
                        ) {
                            Text(text = "Elimina il profilo")
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { authViewModel.logout(appwriteConfig) },
                            modifier = Modifier.width(150.dp)
                        ) {
                            Text(text = "Logout")
                        }
                    }
                }


            }
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
                            if (order.dataAcquisto == null) Text(text = stringResource(id = R.string.unavailable))
                            else Text(text = "${order.dataAcquisto}")
                        }
                    }

                }
            }
        }
    }
}


