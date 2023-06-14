package com.trenicalea.trintedapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.trenicalea.trintedapp.appwrite.AppwriteConfig
import com.trenicalea.trintedapp.models.UtenteDto
import com.trenicalea.trintedapp.ui.theme.TrintedAppTheme
import com.trenicalea.trintedapp.viewmodels.ArticoloViewModel
import com.trenicalea.trintedapp.viewmodels.AuthViewModel
import com.trenicalea.trintedapp.viewmodels.UtenteViewModel
import java.io.FileNotFoundException


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrintedAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomePage(AppwriteConfig(this.applicationContext), this)
                }
            }
        }
    }


}

@Composable
fun TrintedBottomBar(selectedIndex: MutableState<Int>) {
    BottomAppBar {
        NavigationBar {
            NavigationBarItem(
                selected = selectedIndex.value == 0,
                onClick = { selectedIndex.value = 0 },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.home))
                        Text("Home")
                    }
                })
            NavigationBarItem(
                selected = selectedIndex.value == 1,
                onClick = { selectedIndex.value = 1 },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                        Text("Cerca")
                    }
                })
            NavigationBarItem(
                selected = selectedIndex.value == 2,
                onClick = { selectedIndex.value = 2 },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.AddCircle,
                            contentDescription = stringResource(R.string.sell)
                        )
                        Text("Vendi")
                    }
                })
            NavigationBarItem(
                selected = selectedIndex.value == 3,
                onClick = { selectedIndex.value = 3 },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = stringResource(R.string.profile)
                        )
                        Text("Profilo")
                    }
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrintedTopBar(navHostController: NavHostController, selectedIndex: MutableState<Int>) {
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val showBackIcon by remember(currentBackStackEntry) { derivedStateOf { navHostController.previousBackStackEntry != null } }
    TopAppBar(title = { Text("") },
        actions = {
            Button(
                onClick = {
                    selectedIndex.value = 1
                },
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(1f),
                shape = CutCornerShape(10),
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search),
                    modifier = Modifier.padding(end = 5.dp),
                    tint = Color.Black
                )
                Text(text = stringResource(R.string.search), color = Color.DarkGray)
            }
        }
    )
}

@Composable
fun decodeUriAsBitmap(uri: Uri?): Bitmap? {
    val context: Context = LocalContext.current
    var bitmap: Bitmap? = null
    bitmap = try {
        BitmapFactory.decodeStream(
            context
                .contentResolver.openInputStream(uri!!)
        )
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        return null
    }
    return bitmap
}

@Composable
fun HomePage(
    appwrite: AppwriteConfig,
    activity: ComponentActivity,
    authViewModel: AuthViewModel = AuthViewModel(),
    utenteViewModel: UtenteViewModel = UtenteViewModel(),
    articoloViewModel: ArticoloViewModel = ArticoloViewModel()
) {
    val (shownBottomSheet, setBottomSheet) = remember { mutableStateOf(false) }
    val navHostController = rememberNavController()
    val selectedIndex = remember { mutableStateOf(0) }
    val selectedIndexFind = remember { mutableStateOf(0) }
    val isRedirected = remember { mutableStateOf(false) }
    val utente: MutableState<UtenteDto?> = remember { mutableStateOf(null) }

    Scaffold(
        topBar = { TrintedTopBar(navHostController, selectedIndex) },
        bottomBar = { TrintedBottomBar(selectedIndex) }) {
        Box(modifier = Modifier.padding(it)) {
            if (selectedIndex.value == 0) {
                authViewModel.checkLogin(appwrite, utenteViewModel)
                isRedirected.value = false
                HomePageActivity(
                    articoloViewModel,
                    utente,
                    selectedIndex,
                    isRedirected,
                    utenteViewModel
                )
            }
            if (selectedIndex.value == 1) {
                authViewModel.checkLogin(appwrite, utenteViewModel)
                isRedirected.value = false
                FindActivity(
                    userViewModel = utenteViewModel,
                    selectedIndex = selectedIndexFind,
                    isRedirected = isRedirected,
                    selectedHomePageIndex = selectedIndex,
                    user = utente
                )
            }
            if (selectedIndex.value == 2) {
                authViewModel.checkLogin(appwrite, utenteViewModel)
                isRedirected.value = false
                AddProductActivity(authViewModel, articoloViewModel, selectedIndex)
            }
            if (selectedIndex.value == 3) {
                authViewModel.checkLogin(appwrite, utenteViewModel)
                if (authViewModel.loading.value) {
                    Text(text = "Loading...")
                } else if (!authViewModel.isLogged.value) {
                    AuthActivity(
                        activity = activity,
                        appwrite = appwrite,
                        authViewModel,
                        utenteViewModel
                    )
                } else if (authViewModel.isLogged.value) {
                    if (!isRedirected.value) {
                        UserProfileActivity(
                            authViewModel.loggedInUser.value!!,
                            authViewModel,
                            appwrite,
                            utenteViewModel,
                            isRedirected,
                            selectedIndex
                        )
                    } else {
                        println(utente.value!!)
                        UserProfileActivity(
                            user = utente.value!!,
                            authViewModel = authViewModel,
                            appwriteConfig = appwrite,
                            utenteViewModel = utenteViewModel,
                            isRedirected,
                            selectedIndex
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun showAlert(
    title: String,
    description: String,
    onDialogClosed: () -> Unit
) {
    AlertDialog(onDismissRequest = {
        onDialogClosed()
    },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = description)
        }, confirmButton = {
            Button(
                onClick = {
                    onDialogClosed()
                }) {
                Text("Chiudi")
            }
        }
    )
}

