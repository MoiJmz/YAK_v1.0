package com.example.yak.data.db

import android.content.Context
import androidx.room.*
import com.example.yak.data.db.daos.*
import com.example.yak.data.db.entities.*

@Database(
    entities = [UsuarioEntity::class, EjercicioEntity::class, ProgresoEntity::class, RachaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class YakDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ejercicioDao(): EjercicioDao
    abstract fun progresoDao(): ProgresoDao
    abstract fun rachaDao(): RachaDao

    companion object {
        @Volatile private var INSTANCE: YakDatabase? = null
        fun getInstance(context: Context): YakDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, YakDatabase::class.java, "yak_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build().also { INSTANCE = it }
            }
        }
    }
}
