package com.example.yak.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,       // único, no puede repetirse
    val correo: String,
    val password: String,
    val rol: String,            // "OWNER", "STAFF", "USUARIO"
    val fotoPerfil: String?     // URI de la imagen como String
)
