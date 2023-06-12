package com.trenicalea.trintedapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trenicalea.trintedapp.models.ArticoloDto
import com.trenicalea.trintedapp.models.Indirizzo
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.viewmodels.CheckoutViewModel

@Composable
fun OrdineFormActivity(
    articoloDto: ArticoloDto,
//    acquirente: Long,
//    utenteViewModel: UtenteViewModel
) {
//    val _acquirente = utenteViewModel.getUser(acquirente)
//    val _venditore = utenteViewModel.getUser(articoloDto.utente)

    val _acquirente = UtenteDto(
        1,
        "Pietro",
        "Macrì",
        credenzialiUsername = "username",
        credenzialiEmail = "pietromacri2000@gmail.com",
        indirizzo = Indirizzo("via dal cazzo", 90, "Mendicino")
    )
    val _venditore = UtenteDto(
        2,
        "Alessandro",
        "Astorino",
        credenzialiUsername = "username",
        credenzialiEmail = "astorix10@gmail.com",
        indirizzo = Indirizzo("via dai coglioni", 69, "SGF")
    )

    val checkoutViewModel = CheckoutViewModel()
    val usaNuovoIndirizzo = remember { mutableStateOf(false) }
    
    var via = remember { mutableStateOf("") }
    var civico = remember { mutableStateOf("") }
    var citta = remember { mutableStateOf("") }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.padding(15.dp)
    ){
        Column() {
            BasicInformations(articoloDto)
            
            if(_acquirente.indirizzo != null)
                SavedAddress(
                    _acquirente,
                    usaNuovoIndirizzo,
                    articoloDto,
                    checkoutViewModel,
                    via,
                    civico,
                    citta
                )
            else{
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = stringResource(id = R.string.CompileAddressForm))
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                ) {
                    AddressForm(via = via, civico = civico, citta = citta )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                ){
                    Button(onClick = {
                        checkoutViewModel.confirmOrder(_acquirente.id, articoloDto, Indirizzo(via.value, civico.value.toInt(), citta.value))

                        println("""
                            utente ${_acquirente.id} conferma ordine di id ${articoloDto.id}
                            a indirizzo ${Indirizzo(via = via.value, numeroCivico = civico.value.toInt(), citta = citta.value)}
                        """.trimIndent())
                    }) {
                        Text(text = stringResource(id = R.string.ConfirmOrder))
                    }
                }
            }

        }

    }


}

@Composable
fun SavedAddress(acquirente: UtenteDto, usaNuovoIndirizzo: MutableState<Boolean>, articoloDto: ArticoloDto, checkoutViewModel: CheckoutViewModel, via: MutableState<String>, civico: MutableState<String>, citta: MutableState<String>) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.SavedAddress),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = "${stringResource(id = R.string.DeliveryTo)} ",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Text(
            text = "${acquirente.nome} ${acquirente.cognome}",
            fontWeight = FontWeight.Light,
            fontSize = 15.sp
        )
    }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        acquirente.indirizzo?.via?.let { Info(stringResource(id = R.string.AddressStreet), it) }
        Info(stringResource(id = R.string.AddressNumber), acquirente.indirizzo?.numeroCivico.toString())
    }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        acquirente.indirizzo?.citta?.let { Info(stringResource(id = R.string.AddressCity), it) }
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        Checkbox(checked = usaNuovoIndirizzo.value,
            onCheckedChange = { usaNuovoIndirizzo.value = it })
        Text(
            text = stringResource(id = R.string.AnotherAddress),
            fontWeight = FontWeight.Light,
            fontSize = 15.sp
        )
    }
    if (usaNuovoIndirizzo.value){
        AddressForm(via, civico, citta)
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        Button(onClick = {
            if (usaNuovoIndirizzo.value){
                checkoutViewModel.confirmOrder(acquirente.id, articoloDto, Indirizzo(via.value, civico.value.toInt(), citta.value))

                println("""
                            utente ${acquirente.id} conferma ordine di id ${articoloDto.id}
                            a indirizzo ${Indirizzo(via = via.value, numeroCivico = civico.value.toInt(), citta = citta.value)}
                            
                            [NUOVO INDIRIZZO]
                        """.trimIndent())
            }
            else{
                acquirente.indirizzo?.let {
                    checkoutViewModel.confirmOrder(acquirente.id, articoloDto,
                        it
                    )
                }

                println("""
                    utente ${acquirente.id} conferma ordine di id ${articoloDto.id}
                    a indirizzo ${acquirente.indirizzo} [VECCHIO INDIRIZZO]
                """.trimIndent())
            }
        }) {
            Text(text = stringResource(id = R.string.ConfirmOrder))
        }
    }

}

@Composable
fun AddressForm(via: MutableState<String>, civico: MutableState<String>, citta: MutableState<String>) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(2f)) {
                TextField(value = via.value,
                    onValueChange = { via.value = it },
                    label = { Text(text = stringResource(id = R.string.AddressStreet)) }
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                TextField(value = civico.value,
                    onValueChange = { civico.value = it },
                    label = { Text(text = stringResource(id = R.string.AddressNumber)) })
            }
        }

        Row(
            horizontalArrangement = Arrangement.Start,
        ) {
            Column(modifier = Modifier.weight(2f)) {
                TextField(value = citta.value,
                    onValueChange = { citta.value = it },
                    label = { Text(text = stringResource(id = R.string.AddressCity)) })
            }
        }
    }
}

@Composable
fun BasicInformations(articoloDto: ArticoloDto) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = "${stringResource(R.string.ItemName)} ",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${if (articoloDto.titolo == "") "nessuna informazione" else articoloDto.titolo.toString()}",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Light
        )
    }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = "${stringResource(R.string.Price)}",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${if (articoloDto.prezzo.toString() == "") "nessuna informazione" else articoloDto.prezzo.toString()}",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
private fun Info(label: String, content: String) {
    Text(
        text = "${label}\t",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
    )
    Text(
        text = "${content}\t",
        fontWeight = FontWeight.Light,
        fontSize = 15.sp
    )
}


