package com.trenicalea.trintedapp

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import com.trenicalea.trintedapp.viewmodels.ArticoloViewModel
import com.trenicalea.trintedapp.viewmodels.AuthViewModel

@Composable
fun AddProductActivity(
    authViewModel: AuthViewModel,
    articoloViewModel: ArticoloViewModel,
    selectedIndex: MutableState<Int>
) {
    val articoloState by articoloViewModel.addArticoloState.collectAsState()
    val pattern = remember { Regex("^\\d*\\.?\\d*\$") }
    val uris: MutableState<List<Uri>> = remember { mutableStateOf(listOf()) }
    val convertImages: MutableState<Boolean> = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { urilist ->
        uris.value = urilist
        convertImages.value = true
    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
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
                    articoloViewModel.addArticolo(
                        authViewModel,
                        articoloState.titolo,
                        articoloState.descrizione,
                        articoloState.prezzo,
                        articoloState.immagini,
                    )
                    openDialog.value = true
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

}