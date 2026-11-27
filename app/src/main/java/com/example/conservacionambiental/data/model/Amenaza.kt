package com.example.conservacionambiental.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "amenazas",
    foreignKeys = [
        ForeignKey(
            entity = Area::class,
            parentColumns = ["id"],
            childColumns = ["idArea"]
        )
    ]
)
data class Amenaza(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val tipo: String,
    val descripcion: String,
    val nivelRiesgo: String,
    val fechaReporte: String,
    val idArea: Long
)