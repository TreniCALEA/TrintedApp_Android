package com.trenicalea.trintedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.trenicalea.trintedapp.ui.theme.TrintedAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrintedAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomePage()
                }
            }
        }
    }
}

@Composable
fun NavigationView(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "HomePage") {

    }
}

@Composable
fun TrintedBottomBar(selectedIndex: MutableState<Int>) {
    BottomAppBar() {
        NavigationBar() {
            NavigationBarItem(selected = selectedIndex.value == 0, onClick = { selectedIndex.value = 0 }, icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.home))
                    Text("Home")
                }
            })
            NavigationBarItem(selected = selectedIndex.value == 1, onClick = { selectedIndex.value = 1 }, icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.search))
                    Text("Cerca")
                }
            })
            NavigationBarItem(selected = selectedIndex.value == 2, onClick = { selectedIndex.value = 2 }, icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.AddCircle, contentDescription = stringResource(R.string.sell))
                    Text("Vendi")
                }
            })
            NavigationBarItem(selected = selectedIndex.value == 3, onClick = { selectedIndex.value = 3 }, icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Email, contentDescription = stringResource(R.string.inbox))
                    Text("Inbox")
                }
            })
            NavigationBarItem(selected = selectedIndex.value == 4, onClick = { selectedIndex.value = 4 }, icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.profile))
                    Text("Profilo")
                }
            })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrintedTopBar() {

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    val (shownBottomSheet, setBottomSheet)  = remember { mutableStateOf(false) }
    val navHostController = rememberNavController()
    val selectedIndex = remember { mutableStateOf(0) }
    Scaffold(topBar = { TrintedTopBar() }, bottomBar = { TrintedBottomBar(selectedIndex)} ) {
        Box(modifier = Modifier.padding(it)) {

        }
    }
}