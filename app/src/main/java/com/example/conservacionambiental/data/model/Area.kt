
package com.example.conservacionambiental.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "areas")
data class Area(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val ubicacion: String,
    val extension: Double,
    val tipo: String,
    val estado: String
)
