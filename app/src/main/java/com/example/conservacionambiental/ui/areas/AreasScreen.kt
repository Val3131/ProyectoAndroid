
package com.example.conservacionambiental.ui.areas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import com.example.conservacionambiental.data.DatabaseProvider
import com.example.conservacionambiental.data.model.Area
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState


@Composable
fun AreasScreen() {


    val context = LocalContext.current
    val database = remember(context) {
        DatabaseProvider.getDatabase(context)
    }
    val areaDao = remember(database) {
        database.areaDao()
    }
    val scope = rememberCoroutineScope()
    val areas by areaDao.obtenerTodas().collectAsState(initial = emptyList())
    var mostrarFormulario by remember { mutableStateOf(false) }
    var areaEnEdicion by remember { mutableStateOf<Area?>(null) }
    var areaParaEliminar by remember { mutableStateOf<Area?>(null) }
    var nombre by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var extension by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Áreas Protegidas",
            style = MaterialTheme.typography.headlineMedium
        )

        if (!mostrarFormulario) {

            Button(
                onClick = {
                    areaEnEdicion = null
                    nombre = ""
                    ubicacion = ""
                    extension = ""
                    tipo = ""
                    estado = ""
                    mostrarFormulario = true
                }
            ) {
                Text("Agregar área")
            }


            if (areas.isEmpty()) {
                Text("No hay áreas registradas")
            } else {
                areas.forEach { area ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = area.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Ubicación: ${area.ubicacion}")
                            Text("Extensión: ${area.extension} hectáreas")
                            Text("Tipo: ${area.tipo}")
                            Text("Estado: ${area.estado}")


                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextButton(
                                    onClick = {
                                        areaEnEdicion = area
                                        nombre = area.nombre
                                        ubicacion = area.ubicacion
                                        extension = area.extension.toString()
                                        tipo = area.tipo
                                        estado = area.estado
                                        mostrarFormulario = true
                                    }
                                ) {
                                    Text("Editar")
                                }

                                TextButton(
                                    onClick = {
                                        areaParaEliminar = area
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
                text = if (areaEnEdicion == null) {
                    "Registrar área protegida"
                } else {
                    "Editar área protegida"
                },
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ubicacion,
                onValueChange = { ubicacion = it },
                label = { Text("Ubicación") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = extension,
                onValueChange = { extension = it },
                label = { Text("Extensión en hectáreas") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo de área") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(

                onClick = {
                    val extensionNumero = extension.toDoubleOrNull()

                    if (
                        nombre.isNotBlank() &&
                        ubicacion.isNotBlank() &&
                        extensionNumero != null &&
                        extensionNumero > 0 &&
                        tipo.isNotBlank() &&
                        estado.isNotBlank()
                    ) {
                        scope.launch {

                            val areaActualizada = Area(
                                id = areaEnEdicion?.id ?: 0,
                                nombre = nombre.trim(),
                                ubicacion = ubicacion.trim(),
                                extension = extensionNumero,
                                tipo = tipo.trim(),
                                estado = estado.trim()
                            )

                            if (areaEnEdicion == null) {
                                areaDao.insertar(areaActualizada)
                            } else {
                                areaDao.actualizar(areaActualizada)
                            }
                            nombre = ""
                            ubicacion = ""
                            extension = ""
                            tipo = ""
                            estado = ""
                            mostrarFormulario = false
                        }
                    }
                },
                        modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar área")
            }

            OutlinedButton(
                onClick = {
                    mostrarFormulario = false
                    areaEnEdicion = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }

    if (areaParaEliminar != null) {
        AlertDialog(
            onDismissRequest = {
                areaParaEliminar = null
            },
            title = {
                Text("Eliminar área protegida")
            },
            text = {
                Text(
                    "¿Deseas eliminar el área ${areaParaEliminar?.nombre}?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val area = areaParaEliminar
                        if (area != null) {
                            scope.launch {
                                areaDao.eliminar(area)
                                areaParaEliminar = null
                            }
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        areaParaEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
