package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "intel_nodes",
    foreignKeys = [
        ForeignKey(
            entity = CaseEntity::class,
            parentColumns = ["id"],
            childColumns = ["caseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["caseId"])]
)
data class IntelNodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val caseId: Long,
    val title: String,
    val category: String, // "Social Media", "Public Records", "EXIF / Imagery", "Corporate / Registry", "Geolocation", "Threat Intel / Network"
    val rawData: String,
    val verificationStatus: String, // "PROVEN" or "UNPROVEN"
    val sourceCount: Int = 1, // >= 2 sources required to qualify as Proven
    val confidenceLevel: String = "MEDIUM", // LOW, MEDIUM, HIGH, VERIFIED
    val sourceUrls: String = "",
    val notes: String = "",
    val pivotClues: String = "", // Follow-up clues for unproven nodes
    val timestamp: Long = System.currentTimeMillis()
)
