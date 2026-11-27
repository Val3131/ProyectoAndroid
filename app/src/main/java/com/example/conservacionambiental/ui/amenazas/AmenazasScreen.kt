package com.example.conservacionambiental.ui.amenazas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.conservacionambiental.data.DatabaseProvider
import com.example.conservacionambiental.data.model.Amenaza
import kotlinx.coroutines.launch

@Composable
fun AmenazasScreen() {

    val context = LocalContext.current

    val database = remember(context) {
        DatabaseProvider.getDatabase(context)
    }

    val amenazaDao = remember(database) {
        database.amenazaDao()
    }

    val scope = rememberCoroutineScope()

    val amenazas by remember(amenazaDao) {
        amenazaDao.obtenerTodas()
    }.collectAsState(initial = emptyList())

    var mostrarFormulario by remember {
        mutableStateOf(false)
    }

    var amenazaEnEdicion by remember {
        mutableStateOf<Amenaza?>(null)
    }

    var tipo by remember {
        mutableStateOf("")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    var nivelRiesgo by remember {
        mutableStateOf("")
    }

    var fechaReporte by remember {
        mutableStateOf("")
    }

    var idArea by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Gestión de Amenazas",
            style = MaterialTheme.typography.headlineMedium
        )

        if (!mostrarFormulario) {

            Button(
                onClick = {
                    amenazaEnEdicion = null
                    tipo = ""
                    descripcion = ""
                    nivelRiesgo = ""
                    fechaReporte = ""
                    idArea = ""
                    mostrarFormulario = true
                }
            ) {
                Text("Agregar amenaza")
            }

            if (amenazas.isEmpty()) {

                Text("No hay amenazas registradas")

            } else {

                amenazas.forEach { amenaza ->

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text(
                                text = amenaza.tipo,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Descripción: ${amenaza.descripcion}")
                            Text("Nivel de riesgo: ${amenaza.nivelRiesgo}")
                            Text("Fecha reporte: ${amenaza.fechaReporte}")
                            Text("ID Área: ${amenaza.idArea}")

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                TextButton(
                                    onClick = {

                                        amenazaEnEdicion = amenaza

                                        tipo = amenaza.tipo
                                        descripcion = amenaza.descripcion
                                        nivelRiesgo = amenaza.nivelRiesgo
                                        fechaReporte = amenaza.fechaReporte
                                        idArea = amenaza.idArea.toString()

                                        mostrarFormulario = true
                                    }
                                ) {
                                    Text("Editar")
                                }

                                TextButton(
                                    onClick = {
                                        scope.launch {
                                            amenazaDao.eliminar(amenaza)
                                        }
                                    }
                                ) {
                                    Text(
                                        text = "Eliminar",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }

        } else {

            Text(
                text = if (amenazaEnEdicion == null)
                    "Registrar amenaza"
                else
                    "Editar amenaza",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nivelRiesgo,
                onValueChange = { nivelRiesgo = it },
                label = { Text("Nivel de Riesgo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fechaReporte,
                onValueChange = { fechaReporte = it },
                label = { Text("Fecha de Reporte") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = idArea,
                onValueChange = { idArea = it },
                label = { Text("ID Área") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                    if (
                        tipo.isNotBlank() &&
                        descripcion.isNotBlank() &&
                        nivelRiesgo.isNotBlank() &&
                        fechaReporte.isNotBlank() &&
                        idArea.isNotBlank()
                    ) {

                        scope.launch {

                            val amenaza = Amenaza(
                                id = amenazaEnEdicion?.id ?: 0,
                                tipo = tipo.trim(),
                                descripcion = descripcion.trim(),
                                nivelRiesgo = nivelRiesgo.trim(),
                                fechaReporte = fechaReporte.trim(),
                                idArea = idArea.toLong()
                            )

                            if (amenazaEnEdicion == null) {
                                amenazaDao.insertar(amenaza)
                            } else {
                                amenazaDao.actualizar(amenaza)
                            }

                            tipo = ""
                            descripcion = ""
                            nivelRiesgo = ""
                            fechaReporte = ""
                            idArea = ""

                            amenazaEnEdicion = null
                            mostrarFormulario = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (amenazaEnEdicion == null)
                        "Guardar amenaza"
                    else
                        "Guardar cambios"
                )
            }

            OutlinedButton(
                onClick = {
                    mostrarFormulario = false
                    amenazaEnEdicion = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}

