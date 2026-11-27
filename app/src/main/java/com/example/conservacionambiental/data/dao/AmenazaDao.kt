package com.example.conservacionambiental.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.conservacionambiental.data.model.Amenaza
import kotlinx.coroutines.flow.Flow

@Dao
interface AmenazaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(amenaza: Amenaza)

    @Query("SELECT * FROM amenazas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<Amenaza>>

    @Query("SELECT * FROM amenazas WHERE id = :id")
    suspend fun obtenerPorId(id: Long): Amenaza?

    @Update
    suspend fun actualizar(amenaza: Amenaza)

    @Delete
    suspend fun eliminar(amenaza: Amenaza)
}