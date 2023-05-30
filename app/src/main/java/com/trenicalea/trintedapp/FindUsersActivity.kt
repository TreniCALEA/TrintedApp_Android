package com.trenicalea.trintedapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.trenicalea.trintedapp.models.UtenteBasicDto
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel

@Composable
fun FindUsers(prefix: String, userViewModel: UtenteViewModel, navHostController: NavHostController) {
    val users: Array<UtenteBasicDto> = userViewModel.getUserByUsernameLikePaged(prefix = prefix, page = 0)?.content ?: arrayOf()
    LazyColumn {
        val userList: List<UtenteBasicDto> = users.toList()
        items(userList) {user ->
            key(user.id) {
                Row {
                    if (user.immagine == null) {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = stringResource(R.string.defaultImage),
                            modifier = Modifier.size(128.dp)
                        )
                    }
                    else {
                        Image(
                            bitmap = user.immagine.asImageBitmap(),
                            contentDescription = stringResource(R.string.accountImage),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(128.dp)
                                .clip(CircleShape)
                                .padding(5.dp)
                        )
                    }
                    Text(text = "${user.credenzialiUsername}", modifier = Modifier.weight(1f))
                }
                Divider()
            }
        }
    }
}