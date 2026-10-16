package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cases")
data class CaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val targetName: String = "",
    val targetHandle: String = "",
    val targetDomain: String = "",
    val locationOfInterest: String = "",
    val caseCategory: String = "Missing Persons / Crisis Intel",
    val description: String = "",
    val status: String = "ACTIVE", // ACTIVE, ARCHIVED, SOLVED
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
