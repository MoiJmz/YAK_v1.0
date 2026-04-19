package com.example.yak.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "racha")
data class RachaEntity(
    @PrimaryKey val usuarioId: Int,
    val dias: Int,
    val ultimaFecha: String     // formato "yyyy-MM-dd"
)
