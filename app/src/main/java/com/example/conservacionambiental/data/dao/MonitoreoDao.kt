package com.example.conservacionambiental.data.dao

import androidx.room.*
import com.example.conservacionambiental.data.model.Monitoreo
import kotlinx.coroutines.flow.Flow

@Dao
interface MonitoreoDao {

    @Insert
    suspend fun insertar(monitoreo: Monitoreo)

    @Query("SELECT * FROM monitoreos ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<Monitoreo>>

    @Query("SELECT * FROM monitoreos WHERE areaId = :areaId ORDER BY fecha DESC")
    fun obtenerPorArea(areaId: Long): Flow<List<Monitoreo>>

    @Query("SELECT * FROM monitoreos WHERE id = :id")
    suspend fun obtenerPorId(id: Long): Monitoreo?

    @Update
    suspend fun actualizar(monitoreo: Monitoreo)

    @Delete
    suspend fun eliminar(monitoreo: Monitoreo)
}
