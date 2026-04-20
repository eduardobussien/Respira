package com.example.respira

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TechniqueDao {

    @Query("SELECT * FROM technique_table ORDER BY id ASC")
    fun getAllTechniques(): Flow<List<BreathingTechnique>>

    // Saves a new custom technique to the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(technique: BreathingTechnique)
}