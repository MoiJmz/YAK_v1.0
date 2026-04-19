package com.example.yak.data.db.daos

import androidx.room.*
import com.example.yak.data.db.entities.RachaEntity

@Dao
interface RachaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun guardar(racha: RachaEntity)
    
    @Query("SELECT * FROM racha WHERE usuarioId = :uid LIMIT 1")
    fun obtener(uid: Int): RachaEntity?
}
