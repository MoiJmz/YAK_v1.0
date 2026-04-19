package com.example.yak.data.db.daos

import androidx.room.*
import com.example.yak.data.db.entities.EjercicioEntity

@Dao
interface EjercicioDao {
    @Insert
    fun insertar(ejercicio: EjercicioEntity)
    
    @Query("SELECT * FROM ejercicios WHERE lengua = :lengua")
    fun obtenerPorLengua(lengua: String): List<EjercicioEntity>
    
    @Query("SELECT * FROM ejercicios")
    fun obtenerTodos(): List<EjercicioEntity>
    
    @Update
    fun actualizar(ejercicio: EjercicioEntity)
    
    @Delete
    fun eliminar(ejercicio: EjercicioEntity)

    @Query("DELETE FROM ejercicios")
    fun eliminarTodo()
}
