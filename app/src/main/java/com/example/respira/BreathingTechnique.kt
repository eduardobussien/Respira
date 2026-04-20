package com.example.respira

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "technique_table")
data class BreathingTechnique(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val inhale: Int,
    val hold: Int,
    val exhale: Int
)