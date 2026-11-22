package com.example.conservacionambiental.ui.areas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AreasScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Áreas Protegidas",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Gestión de áreas protegidas",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = {}
        ) {
            Text("Agregar área")
        }

        Text("No hay áreas registradas")
    }
}
