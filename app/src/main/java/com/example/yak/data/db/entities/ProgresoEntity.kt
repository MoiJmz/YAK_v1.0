package com.example.yak.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progreso")
data class ProgresoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,         // FK al id del usuario
    val ejercicioId: Int,       // FK al id del ejercicio
    val completado: Boolean,
    val fecha: String           // formato "yyyy-MM-dd"
)
