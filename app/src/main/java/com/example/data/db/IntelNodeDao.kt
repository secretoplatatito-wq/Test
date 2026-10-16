package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.IntelNodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IntelNodeDao {
    @Query("SELECT * FROM intel_nodes WHERE caseId = :caseId ORDER BY timestamp DESC")
    fun getNodesForCase(caseId: Long): Flow<List<IntelNodeEntity>>

    @Query("SELECT * FROM intel_nodes WHERE caseId = :caseId AND verificationStatus = :status ORDER BY timestamp DESC")
    fun getNodesByStatus(caseId: Long, status: String): Flow<List<IntelNodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNode(node: IntelNodeEntity): Long

    @Update
    suspend fun updateNode(node: IntelNodeEntity)

    @Delete
    suspend fun deleteNode(node: IntelNodeEntity)

    @Query("SELECT COUNT(*) FROM intel_nodes WHERE caseId = :caseId")
    suspend fun getNodeCountForCase(caseId: Long): Int

    @Query("SELECT COUNT(*) FROM intel_nodes WHERE caseId = :caseId AND verificationStatus = 'PROVEN'")
    suspend fun getProvenCountForCase(caseId: Long): Int

    @Query("SELECT COUNT(*) FROM intel_nodes WHERE caseId = :caseId AND verificationStatus = 'UNPROVEN'")
    suspend fun getUnprovenCountForCase(caseId: Long): Int
}
