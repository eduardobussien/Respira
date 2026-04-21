package com.example.respira

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TechniqueDao {

    @Query("SELECT * FROM technique_table ORDER BY id ASC")
    fun getAllTechniques(): Flow<List<BreathingTechnique>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(technique: BreathingTechnique)

    @Delete
    suspend fun delete(technique: BreathingTechnique)

    @Update
    suspend fun update(technique: BreathingTechnique)
}