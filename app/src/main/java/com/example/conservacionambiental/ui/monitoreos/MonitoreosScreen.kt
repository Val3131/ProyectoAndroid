
package com.example.conservacionambiental.ui.monitoreos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.conservacionambiental.data.DatabaseProvider
import com.example.conservacionambiental.data.model.Monitoreo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonitoreosScreen() {

    val context = LocalContext.current
    val database = remember(context) {
        DatabaseProvider.getDatabase(context)
    }

    val monitoreoDao = remember(database) {
        database.monitoreoDao()
    }

    val areaDao = remember(database) {
        database.areaDao()
    }

    val monitoreos by remember(monitoreoDao) {
        monitoreoDao.obtenerTodos()
    }.collectAsState(initial = emptyList())

    val areas by remember(areaDao) {
        areaDao.obtenerTodas()
    }.collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()

    var mostrarFormulario by remember {
        mutableStateOf(false)
    }

    var monitoreoEnEdicion by remember {
        mutableStateOf<Monitoreo?>(null)
    }
    var monitoreoParaEliminar by remember {
        mutableStateOf<Monitoreo?>(null)
    }

    var areaSeleccionadaId by remember {
        mutableStateOf<Long?>(null)
    }

    var fecha by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }
    var mostrarSelectorAreas by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Monitoreos",
            style = MaterialTheme.typography.headlineMedium
        )

        if (!mostrarFormulario) {

            Button(
                onClick = {
                    monitoreoEnEdicion = null
                    areaSeleccionadaId = null
                    fecha = ""
                    tipo = ""
                    resultado = ""
                    mostrarSelectorAreas = false
                    mostrarFormulario = true
                }
            ) {
                Text("Agregar monitoreo")
            }

            if (monitoreos.isEmpty()) {
                Text("No hay monitoreos registrados")
            } else {

                monitoreos.forEach { monitoreo ->

                    val nombreArea = areas.find {
                        it.id == monitoreo.areaId
                    }?.nombre ?: "Área no encontrada"

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text(
                                text = monitoreo.tipo,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Fecha: ${monitoreo.fecha}")
                            Text("Área protegida: $nombreArea")
                            Text("Resultado: ${monitoreo.resultado}")


                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextButton(
                                    onClick = {
                                        monitoreoEnEdicion = monitoreo
                                        areaSeleccionadaId = monitoreo.areaId
                                        fecha = monitoreo.fecha
                                        tipo = monitoreo.tipo
                                        resultado = monitoreo.resultado
                                        mostrarSelectorAreas = false
                                        mostrarFormulario = true
                                    }
                                ) {
                                    Text("Editar")
                                }

                                TextButton(
                                    onClick = {
                                        monitoreoParaEliminar = monitoreo
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
                text = if (monitoreoEnEdicion == null) {
                    "Registrar monitoreo"
                } else {
                    "Editar monitoreo"
                },
                style = MaterialTheme.typography.titleLarge
            )

            val areaSeleccionada = areas.find {
                it.id == areaSeleccionadaId
            }

            ExposedDropdownMenuBox(
                expanded = mostrarSelectorAreas,
                onExpandedChange = {
                    mostrarSelectorAreas = it
                }
            ) {

                OutlinedTextField(
                    value = areaSeleccionada?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Área protegida")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = mostrarSelectorAreas
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = mostrarSelectorAreas,
                    onDismissRequest = {
                        mostrarSelectorAreas = false
                    }
                ) {

                    areas.forEach { area ->
                        DropdownMenuItem(
                            text = {
                                Text(area.nombre)
                            },
                            onClick = {
                                areaSeleccionadaId = area.id
                                mostrarSelectorAreas = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = fecha,
                onValueChange = {
                    fecha = it
                },
                label = {
                    Text("Fecha (AAAA-MM-DD)")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tipo,
                onValueChange = {
                    tipo = it
                },
                label = {
                    Text("Tipo de monitoreo")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = resultado,
                onValueChange = {
                    resultado = it
                },
                label = {
                    Text("Resultado")
                },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val id = areaSeleccionadaId

                    if (
                        id != null &&
                        areas.any { it.id == id } &&
                        fecha.isNotBlank() &&
                        tipo.isNotBlank() &&
                        resultado.isNotBlank()
                    ) {

                        scope.launch {

                            val monitoreoActualizado = Monitoreo(
                                id = monitoreoEnEdicion?.id ?: 0,
                                areaId = id,
                                fecha = fecha.trim(),
                                tipo = tipo.trim(),
                                resultado = resultado.trim()
                            )

                            if (monitoreoEnEdicion == null) {
                                monitoreoDao.insertar(monitoreoActualizado)
                            } else {
                                monitoreoDao.actualizar(monitoreoActualizado)
                            }

                            monitoreoEnEdicion = null
                            areaSeleccionadaId = null
                            fecha = ""
                            tipo = ""
                            resultado = ""
                            mostrarSelectorAreas = false
                            mostrarFormulario = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (monitoreoEnEdicion == null) {
                        "Guardar monitoreo"
                    } else {
                        "Guardar cambios"
                    }
                )
            }

            OutlinedButton(
                onClick = {
                    monitoreoEnEdicion = null
                    areaSeleccionadaId = null
                    fecha = ""
                    tipo = ""
                    resultado = ""
                    mostrarSelectorAreas = false
                    mostrarFormulario = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
    monitoreoParaEliminar?.let { monitoreo ->

        AlertDialog(
            onDismissRequest = {
                monitoreoParaEliminar = null
            },
            title = {
                Text("Eliminar monitoreo")
            },
            text = {
                Text("¿Estás segura de que deseas eliminar este monitoreo? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            monitoreoDao.eliminar(monitoreo)
                            monitoreoParaEliminar = null
                        }
                    }
                ) {
                    Text(
                        "Eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        monitoreoParaEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
