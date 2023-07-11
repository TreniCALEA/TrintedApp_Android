package com.trenicalea.trintedapp

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.viewmodels.ArticoloViewModel
import com.trenicalea.trintedapp.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductActivity(
    appwriteConfig: AppwriteConfig,
    authViewModel: AuthViewModel,
    articoloViewModel: ArticoloViewModel,
    selectedIndex: MutableState<Int>,
    onDismissRequest: () -> Unit
) {
    val articoloState by articoloViewModel.addArticoloState.collectAsState()
    val pattern = remember { Regex("^\\d*\\.?\\d*\$") }
    val sheetState = rememberModalBottomSheetState()

    val uris: MutableState<List<Uri>> = remember { mutableStateOf(listOf()) }
    val convertImages: MutableState<Boolean> = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
    val openErrorDialog = remember { mutableStateOf(false) }
    val fieldsError by remember { derivedStateOf { articoloState.descrizioneHasError || articoloState.titoloHasError || articoloState.prezzoHasError || articoloState.immagini.isEmpty() } }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { urilist ->
        uris.value = urilist
        convertImages.value = true
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        // Headline text
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                text = "Inizia a vendere!",
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 25.sp)
            )
        }

        // Titolo dell'articolo
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = articoloState.titolo,
                label = { Text(text = "Titolo dell'articolo") },
                onValueChange = { articoloViewModel.updateTitolo(it) }
            )
        }

        // Descrizione dell'articolo
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = articoloState.descrizione,
                label = { Text(text = "Descrizione dell'articolo") },
                onValueChange = { articoloViewModel.updateDescrizione(it) }
            )
        }

        // Prezzo dell'articolo
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = "${articoloState.prezzo}",
                label = { Text(text = "Prezzo dell'articolo") },
                onValueChange = {
                    if (it.isEmpty() || it.matches(pattern))
                        articoloViewModel.updatePrezzo(it.toDouble())
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        if (articoloState.immagini.isEmpty()) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "Aggiungi delle immagini per continuare!")
            }
        }

        // Add the "Choose Images" button
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(
                onClick = {
                    filePickerLauncher.launch("image/*") // Pick images only
                }
            ) {
                Text(text = "Choose Images")
            }
        }

        if (articoloState.immagini.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "Immagini selezionate: ${articoloState.immagini.size}")
            }
        }

        // Bottone "Vendi"
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(
                onClick = {
                    if (!fieldsError) {
                        articoloViewModel.addArticolo(
                            appwriteConfig,
                            authViewModel,
                            articoloState.titolo,
                            articoloState.descrizione,
                            articoloState.prezzo,
                            articoloState.immagini,
                        )
                        openDialog.value = true
                    } else {
                        openErrorDialog.value = true
                    }
                }
            ) {
                Text(text = "Vendi!")
            }
        }
    }

    if (convertImages.value) {
        articoloViewModel.updateImmagini(uris.value)
        convertImages.value = false
    }

    if (openDialog.value) {
        showAlert(
            "Articolo aggiunto",
            "Il tuo articolo è stato pubblicato correttamente",
        ) {
            openDialog.value = false
            selectedIndex.value = 0
        }
    }

    if (openErrorDialog.value) {
        showAlert(
            "Attenzione!",
            "Controlla di aver inserito tutto il necessario prima di procedere!"
        ) {
            openErrorDialog.value = false
        }
    }
}