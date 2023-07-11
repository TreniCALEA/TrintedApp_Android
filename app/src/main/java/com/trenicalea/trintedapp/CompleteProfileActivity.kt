package com.trenicalea.trintedapp

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.Indirizzo
import com.trenicalea.trintedapp.viewmodels.AuthViewModel
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteProfile(
    appwriteConfig: AppwriteConfig,
    authViewModel: AuthViewModel,
    utenteViewModel: UtenteViewModel,
    selectedIndex: MutableState<Int>,
    onClose: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    val userUpdateState by utenteViewModel.userUpdateState.collectAsState()
    val immagine = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        utenteViewModel.updateImmagine(it)
    }

    val openDialog = remember { mutableStateOf(false) }
    val openErrorDialog = remember { mutableStateOf(false) }
    val imageEmpty = remember { mutableStateOf(true) }

    val addressError by remember {
        derivedStateOf {
            userUpdateState.citta.isEmpty() ||
                    userUpdateState.via.isEmpty() ||
                    userUpdateState.civico == 0
        }
    }

    val fullNameError by remember {
        derivedStateOf {
            userUpdateState.nome.isEmpty() ||
                    userUpdateState.cognome.isEmpty()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                userUpdateState.immagine?.let {
                    imageEmpty.value = false
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = stringResource(R.string.accountImage),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .padding(5.dp)
                    )
                } ?: Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = stringResource(R.string.defaultImage),
                    modifier = Modifier
                        .size(128.dp)
                        .padding(5.dp)
                )
                Card(modifier = Modifier.align(Alignment.BottomCenter)) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(R.string.accountImage),
                        modifier = Modifier
                            .size(40.dp)
                            .padding(5.dp)
                            .clickable { immagine.launch() }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = userUpdateState.nome,
                    onValueChange = { utenteViewModel.updateNome(it) },
                    label = { Text("Nome") }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(value = userUpdateState.cognome,
                    onValueChange = { utenteViewModel.updateCognome(it) },
                    label = { Text("Cognome") })
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(value = userUpdateState.citta,
                        onValueChange = { utenteViewModel.updateCitta(it) },
                        label = { Text("Citta") })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(value = userUpdateState.via,
                        onValueChange = { utenteViewModel.updateVia(it) },
                        label = { Text("Via") })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = userUpdateState.civico.toString(),
                        onValueChange = { civicoString: String ->
                            if (civicoString == "") utenteViewModel.updateCivico(0)
                            else utenteViewModel.updateCivico(civicoString.toInt())
                        },
                        label = { Text("Civico") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(onClick = {
                    if (fullNameError && addressError && imageEmpty.value) {
                        openErrorDialog.value = true
                    } else if (!fullNameError && !addressError) {
                        utenteViewModel.updateUser(
                            appwriteConfig,
                            authViewModel,
                            userUpdateState.nome,
                            userUpdateState.cognome,
                            userUpdateState.immagine,
                            Indirizzo(
                                userUpdateState.via,
                                userUpdateState.civico,
                                userUpdateState.citta
                            )
                        )
                        openDialog.value = true
                    } else if (fullNameError) {
                        utenteViewModel.updateUser(
                            appwriteConfig,
                            authViewModel,
                            userUpdateState.nome,
                            userUpdateState.cognome,
                            userUpdateState.immagine,
                            Indirizzo(
                                userUpdateState.via,
                                userUpdateState.civico,
                                userUpdateState.citta
                            )
                        )
                        openDialog.value = true
                    } else if (addressError) {
                        utenteViewModel.updateUser(
                            appwriteConfig,
                            authViewModel,
                            userUpdateState.nome,
                            userUpdateState.cognome,
                            userUpdateState.immagine,
                            null
                        )
                        openDialog.value = true
                    }
                }) {
                    Text("Aggiorna il profilo")
                }
            }
        }
    }

    if (openErrorDialog.value) {
        showAlert(
            title = "Campi non correttamente riempiti",
            description = "Tutti i campi devono essere compilati correttamente. " +
                    "Non si possono lasciare in bianco campi correlati ad altri già compilati," +
                    " inoltre il numero civico non può essere 0!"
        ) {
            openErrorDialog.value = false
        }
    }

    if (openDialog.value) {
        showAlert(
            "Profilo aggiornato",
            "Il profilo è stato aggiornato correttamente!",
        ) {
            openDialog.value = false
            selectedIndex.value = 0
        }
    }
}