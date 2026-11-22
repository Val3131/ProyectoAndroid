package com.example.conservacionambiental.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.conservacionambiental.data.model.Area
import kotlinx.coroutines.flow.Flow

@Dao
interface AreaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(area: Area)

    @Query("SELECT * FROM areas ORDER BY nombre ASC")
    fun obtenerTodas(): Flow<List<Area>>

    @Query("SELECT * FROM areas WHERE id = :id")
    suspend fun obtenerPorId(id: Long): Area?

    @Update
    suspend fun actualizar(area: Area)

    @Delete
    suspend fun eliminar(area: Area)
}
