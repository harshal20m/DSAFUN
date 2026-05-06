package com.dsafun.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dsafun.app.data.local.entity.UserSolutionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSolutionDao {
    @Query("SELECT * FROM user_solutions WHERE problemId = :problemId AND language = :language")
    fun getSolutionForProblem(problemId: Int, language: String): Flow<UserSolutionEntity?>

    @Query("SELECT * FROM user_solutions WHERE problemId = :problemId")
    fun getAllSolutionsForProblem(problemId: Int): Flow<List<UserSolutionEntity>>
    
    @Query("SELECT * FROM user_solutions ORDER BY lastEditedAt DESC LIMIT :limit")
    suspend fun getRecentSolutionsSync(limit: Int): List<UserSolutionEntity>
    
    @Query("SELECT * FROM user_solutions WHERE status = 'SOLVED'")
    suspend fun getSolvedProblemsSync(): List<UserSolutionEntity>
    
    @Query("SELECT * FROM user_solutions")
    fun getAllSolutions(): Flow<List<UserSolutionEntity>>

    @Upsert
    suspend fun upsertSolution(solution: UserSolutionEntity)

    @Query("DELETE FROM user_solutions WHERE problemId = :problemId AND language = :language")
    suspend fun deleteSolution(problemId: Int, language: String)
}

// Made with Bob