package com.trenicalea.trintedapp


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.trenicalea.trintedapp.viewmodels.OrdineViewModel
import com.trenicalea.trintedapp.viewmodels.AuthViewModel
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel


@Composable
fun UserProfileActivity(
    user: UtenteDto,
    authViewModel: AuthViewModel,
    appwriteConfig: AppwriteConfig,
    utenteViewModel: UtenteViewModel
) {
    val ordineViewModel = OrdineViewModel(user.id)

    val purchasesList = ordineViewModel.ordersGetByAcquirente ?: arrayOf()
    val salesList = ordineViewModel.ordersGetByVenditore ?: arrayOf()

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Column {
            if (!utenteViewModel.isChecked.value) {
                Row {
                    Button(
                        onClick = { utenteViewModel.checkVerified(appwriteConfig) },
                    ) {
                        Text(text = "Verifica account")
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                if (user.image != null) {
                    Image(
                        bitmap = user.image.asImageBitmap(),
                        contentDescription = "${stringResource(id = R.string.propic)} ${user.nome} ${user.cognome}"
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
                    text = "${user.nome} ${user.cognome}",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 25.sp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
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

            if (salesList.isNotEmpty()) Carousel(
                list = salesList, title = stringResource(R.string.recentSales)
            )
            else ArrayEmpty()

            Divider()

            if (purchasesList.isNotEmpty()) Carousel(
                list = purchasesList, title = stringResource(R.string.recentPurchases)
            )
            else ArrayEmpty()
        }

        Row {
            Button(
                onClick = { authViewModel.logout(appwriteConfig) },
            ) {
                Text(text = "Logout")
            }
        }
    }
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
                            // Use bitmapAdapter
                        }
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


