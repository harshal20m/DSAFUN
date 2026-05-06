package com.dsafun.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dsafun.app.data.local.entity.ProblemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemDao {
    @Query("SELECT * FROM problems")
    fun getAllProblems(): Flow<List<ProblemEntity>>
    
    @Query("SELECT * FROM problems")
    suspend fun getAllProblemsSync(): List<ProblemEntity>

    @Query("SELECT * FROM problems WHERE id = :id")
    fun getProblemById(id: Int): Flow<ProblemEntity?>

    @Query("SELECT * FROM problems WHERE topic = :topic")
    fun getProblemsByTopic(topic: String): Flow<List<ProblemEntity>>

    @Query("SELECT * FROM problems WHERE difficulty = :difficulty")
    fun getProblemsByDifficulty(difficulty: String): Flow<List<ProblemEntity>>

    @Query("SELECT * FROM problems WHERE title LIKE '%' || :query || '%'")
    fun searchProblems(query: String): Flow<List<ProblemEntity>>

    @Query("""
        SELECT * FROM problems
        WHERE (:topic IS NULL OR topic = :topic)
        AND (:difficulty IS NULL OR difficulty = :difficulty)
        AND (:searchQuery = '' OR title LIKE '%' || :searchQuery || '%')
    """)
    fun getFilteredProblems(
        topic: String?,
        difficulty: String?,
        searchQuery: String
    ): Flow<List<ProblemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProblems(problems: List<ProblemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProblem(problem: ProblemEntity)
}

// Made with Bob
