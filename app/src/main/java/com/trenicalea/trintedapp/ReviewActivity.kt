package com.trenicalea.trintedapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.Recensione
import com.trenicalea.trintedapp.viewmodels.RecensioneViewModel

@Composable
fun ReviewActivity(
    appwriteConfig: AppwriteConfig,
    recensioneViewModel: RecensioneViewModel
) {
    val reviewList: List<Recensione> = recensioneViewModel.reviewList.value

    if (reviewList.isEmpty()){
        Text(text = "Non è presente alcuna recensione")
    }
    else{
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
                        Text(text = review.autore.credenziali.username, modifier = Modifier.weight(1f))
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
}