import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trenicalea.trintedapp.R
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.viewmodels.AuthViewModel
import com.trenicalea.trintedapp.viewmodels.ReviewViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReviewActivity(
    appwriteConfig: AppwriteConfig,
    reviewViewModel: ReviewViewModel,
    utenteDto: UtenteDto,
    authViewModel: AuthViewModel
) {
    reviewViewModel.getUserReview(utenteDto)
    val reviewState by reviewViewModel.reviewState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val listItems = arrayOf(1.0f, 2.0f, 3.0f, 4.0f, 5.0f)
    var selectedRating by remember { mutableStateOf(listItems[0]) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (reviewViewModel.reviewList.value.isEmpty()) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Non è presente alcuna recensione")
            }
        } else {
            LazyColumn {
                items(reviewViewModel.reviewList.value) { review ->
                    key(review.id) {
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
        if (!reviewViewModel.isSameUser(authViewModel, utenteDto)) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            {
                Box(
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        },
                    ) {
                        TextField(
                            label = { Text(text = "Rating: ") },
                            value = "$selectedRating",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listItems.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = "$item") },
                                    onClick = {
                                        selectedRating = item
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                isError = reviewState.descriptionHasError,
                value = reviewState.description,
                onValueChange = { reviewViewModel.updateDescrizione(it) },
                label = { Text("Lascia una recensione qui...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            )
            Button(
                onClick = {
                    reviewViewModel.addReview(
                        authViewModel,
                        reviewState.description,
                        selectedRating,
                        utenteDto
                    )
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = "Invia")
            }
        }
    }
}

