package com.example.yak.data.db.daos

import androidx.room.*
import com.example.yak.data.db.entities.ProgresoEntity

@Dao
interface ProgresoDao {
    @Insert
    fun insertar(progreso: ProgresoEntity)
    
    @Query("SELECT * FROM progreso WHERE usuarioId = :uid AND ejercicioId = :eid LIMIT 1")
    fun buscar(uid: Int, eid: Int): ProgresoEntity?
    
    @Query("SELECT * FROM progreso WHERE usuarioId = :uid AND completado = 1")
    fun obtenerCompletados(uid: Int): List<ProgresoEntity>
}
