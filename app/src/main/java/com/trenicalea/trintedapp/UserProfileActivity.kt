package com.trenicalea.trintedapp


import android.graphics.Bitmap
import android.graphics.drawable.Icon
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ProductionQuantityLimits
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trenicalea.trintedapp.models.OrdineDto
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.viewmodels.OrdineViewModel
import java.time.LocalDate


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun UserProfileActivity(
    user: UtenteDto = UtenteDto(
        1,
        "pietro",
        "macrì",
        "pietro.macri2000@gmail.com",
        false
    )
) {
//      val purchasesList = OrdineViewModel().getByAcquirente(user.id)
//      val salesList = OrdineViewModel().getByVenditore(user.id)

    val purchasesList = arrayOf<OrdineDto>(
        OrdineDto(1, LocalDate.now()),
        OrdineDto(2, LocalDate.now()),
        OrdineDto(3, LocalDate.now()),
        OrdineDto(4, LocalDate.now())
    )

    val salesList = arrayOf<OrdineDto>(
//        OrdineDto(1, LocalDate.now()),
//        OrdineDto(2, LocalDate.now()),
//        OrdineDto(3, LocalDate.now()),
//        OrdineDto(4, LocalDate.now())
    )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {

        Column {
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
                Row() {
                    Text(
                        text = "${user.credenzialiEmail}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Divider()

            if(salesList.isNotEmpty())
                Carousel(list = salesList, title = stringResource(R.string.recentSales))
            else
                arrayEmpty()

            Divider()

            if(purchasesList.isNotEmpty())
                Carousel(list = purchasesList, title = stringResource(R.string.recentPurchases))
            else
                arrayEmpty()


        }
    }
}

@Composable
fun arrayEmpty(){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        Icon(imageVector = Icons.Filled.ProductionQuantityLimits ,
            contentDescription = stringResource(id = R.string.noItems),
            modifier = Modifier.size(40.dp))
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
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
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
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
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Row() {
                            val orderImage: Bitmap? = null
                            if (orderImage == null) {
                                Icon(
                                    imageVector = Icons.Filled.HideImage,
                                    contentDescription = stringResource(id = R.string.noImages),
                                    modifier = Modifier.size(120.dp)
                                )
                            } else {

                            }
                        }
                        Row() {
                            Icon(
                                imageVector = Icons.Filled.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            if(order.dataAcquisto == null)
                                Text(text = stringResource(id = R.string.unavailable))
                            else
                                Text(text = "${order.dataAcquisto}")
                        }
                    }

                }
            }
        }
    }
}


