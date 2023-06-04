package com.trenicalea.trintedapp


import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trenicalea.trintedapp.models.OrdineDto
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel
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
    val purchasesPagerState = rememberPagerState(initialPage = 1)
    val purchasesList = listOf<OrdineDto>()

    val salesPagerState = rememberPagerState(initialPage = 1)
    val salesList = listOf<OrdineDto>(
        OrdineDto(1, LocalDate.now()),
        OrdineDto(2, LocalDate.now()),
        OrdineDto(3, LocalDate.now()),
        OrdineDto(4, LocalDate.now())
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
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

            }
            Divider()

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                Text(text = stringResource(id = R.string.recentSales))

            }
        }

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = stringResource(id = R.string.recentPurchases))
        }
    }

}