package com.example.conservacionambiental

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.conservacionambiental.ui.amenazas.AmenazasScreen
import com.example.conservacionambiental.ui.areas.AreasScreen
import com.example.conservacionambiental.ui.monitoreos.MonitoreosScreen
import com.example.conservacionambiental.ui.theme.ConservacionAmbientalTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConservacionAmbientalTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaPrincipal()
                }
            }
        }
    }
}

@Composable
fun PantallaPrincipal() {

    var pantallaActual by remember {
        mutableStateOf("areas")
    }

    Scaffold(
        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = pantallaActual == "areas",
                    onClick = {
                        pantallaActual = "areas"
                    },
                    icon = {
                        Text("A")
                    },
                    label = {
                        Text("Áreas")
                    }
                )

                NavigationBarItem(
                    selected = pantallaActual == "monitoreos",
                    onClick = {
                        pantallaActual = "monitoreos"
                    },
                    icon = {
                        Text("M")
                    },
                    label = {
                        Text("Monitoreos")
                    }
                )

                NavigationBarItem(
                    selected = pantallaActual == "amenazas",
                    onClick = {
                        pantallaActual = "amenazas"
                    },
                    icon = {
                        Text("T")
                    },
                    label = {
                        Text("Amenazas")
                    }
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (pantallaActual) {

                "areas" -> AreasScreen()

                "monitoreos" -> MonitoreosScreen()

                "amenazas" -> AmenazasScreen()
            }
        }
    }
}