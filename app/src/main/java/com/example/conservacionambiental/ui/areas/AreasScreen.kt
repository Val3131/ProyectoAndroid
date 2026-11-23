
package com.example.conservacionambiental.ui.areas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.conservacionambiental.data.DatabaseProvider
import com.example.conservacionambiental.data.model.Area
import kotlinx.coroutines.launch

@Composable
fun AreasScreen() {

    val context = LocalContext.current

    val database = remember(context) {
        DatabaseProvider.getDatabase(context)
    }

    val areaDao = remember(database) {
        database.areaDao()
    }

    val monitoreoDao = remember(database) {
        database.monitoreoDao()
    }

    val scope = rememberCoroutineScope()

    val areas by remember(areaDao) {
        areaDao.obtenerTodas()
    }.collectAsState(initial = emptyList())

    var mostrarFormulario by remember {
        mutableStateOf(false)
    }

    var areaEnEdicion by remember {
        mutableStateOf<Area?>(null)
    }

    var areaParaEliminar by remember {
        mutableStateOf<Area?>(null)
    }

    var mensajeEliminacion by remember {
        mutableStateOf<String?>(null)
    }

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
                                modifier = Modifier.fillMaxWidth(),
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
                onValueChange = {
                    nombre = it
                },
                label = {
                    Text("Nombre")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ubicacion,
                onValueChange = {
                    ubicacion = it
                },
                label = {
                    Text("Ubicación")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = extension,
                onValueChange = {
                    extension = it
                },
                label = {
                    Text("Extensión en hectáreas")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tipo,
                onValueChange = {
                    tipo = it
                },
                label = {
                    Text("Tipo de área")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado,
                onValueChange = {
                    estado = it
                },
                label = {
                    Text("Estado")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                    val extensionNumero = extension.toDoubleOrNull()

                    if (
                        nombre.isNotBlank() &&
                        ubicacion.isNotBlank() &&
                        extensionNumero != null &&
                        extensionNumero.isFinite() &&
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

                            areaEnEdicion = null
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
                Text(
                    if (areaEnEdicion == null) {
                        "Guardar área"
                    } else {
                        "Guardar cambios"
                    }
                )
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

    areaParaEliminar?.let { area ->

        AlertDialog(
            onDismissRequest = {
                areaParaEliminar = null
            },
            title = {
                Text("Eliminar área protegida")
            },
            text = {
                Text("¿Deseas eliminar el área '${area.nombre}'?")
            },
            confirmButton = {
                TextButton(
                    onClick = {

                        areaParaEliminar = null

                        scope.launch {

                            val cantidadMonitoreos =
                                monitoreoDao.contarPorArea(area.id)

                            if (cantidadMonitoreos > 0) {

                                mensajeEliminacion =
                                    "No se puede eliminar el área '${area.nombre}' porque tiene $cantidadMonitoreos monitoreo(s) asociado(s). Debes eliminar primero esos monitoreos."

                            } else {

                                try {
                                    areaDao.eliminar(area)
                                } catch (e: android.database.sqlite.SQLiteConstraintException) {
                                    mensajeEliminacion =
                                        "No se puede eliminar el área '${area.nombre}' porque tiene registros asociados."
                                } catch (e: androidx.sqlite.SQLiteException) {
                                    mensajeEliminacion =
                                        "No se pudo eliminar el área '${area.nombre}'. Verifica que no tenga registros asociados."
                                }
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
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

    mensajeEliminacion?.let { mensaje ->

        AlertDialog(
            onDismissRequest = {
                mensajeEliminacion = null
            },
            title = {
                Text("No se puede eliminar el área")
            },
            text = {
                Text(mensaje)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mensajeEliminacion = null
                    }
                ) {
                    Text("Entendido")
                }
            }
        )
    }
}