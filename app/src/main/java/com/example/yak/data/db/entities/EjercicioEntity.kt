package com.example.yak.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ejercicios")
data class EjercicioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lengua: String,         // "Chol", "Maya", "LSM"
    val titulo: String,
    val nivel: String,          // "Básico", "Intermedio"
    val pregunta: String,
    val opcion1: String,
    val opcion2: String,
    val opcion3: String,
    val opcion4: String,
    val respuestaCorrecta: String,
    val imagenUri: String?
)
