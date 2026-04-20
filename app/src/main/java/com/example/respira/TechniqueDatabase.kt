package com.example.respira

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BreathingTechnique::class], version = 1, exportSchema = false)
abstract class TechniqueDatabase : RoomDatabase() {

    abstract fun techniqueDao(): TechniqueDao

    companion object {
        @Volatile
        private var INSTANCE: TechniqueDatabase? = null

        fun getDatabase(context: Context): TechniqueDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TechniqueDatabase::class.java,
                    "technique_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}