package com.example.yak.data.db.daos

import androidx.room.*
import com.example.yak.data.db.entities.UsuarioEntity

@Dao
interface UsuarioDao {
    @Insert
    fun insertar(usuario: UsuarioEntity)
    
    @Query("SELECT * FROM usuarios WHERE username = :username LIMIT 1")
    fun buscarPorUsername(username: String): UsuarioEntity?
    
    @Query("SELECT * FROM usuarios WHERE username = :u AND password = :p LIMIT 1")
    fun login(u: String, p: String): UsuarioEntity?
    
    @Query("SELECT * FROM usuarios")
    fun obtenerTodos(): List<UsuarioEntity>
    
    @Update
    fun actualizar(usuario: UsuarioEntity)
    
    @Delete
    fun eliminar(usuario: UsuarioEntity)
}
