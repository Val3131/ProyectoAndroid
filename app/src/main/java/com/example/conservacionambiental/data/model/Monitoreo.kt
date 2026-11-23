package com.example.conservacionambiental.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "monitoreos",
    foreignKeys = [
        ForeignKey(
            entity = Area::class,
            parentColumns = ["id"],
            childColumns = ["areaId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["areaId"])]
)
data class Monitoreo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val areaId: Long,
    val fecha: String,
    val tipo: String,
    val resultado: String
)
