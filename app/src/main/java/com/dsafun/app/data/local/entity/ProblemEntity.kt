package com.dsafun.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dsafun.app.data.local.model.Example
import com.dsafun.app.data.local.model.TestCase

@Entity(tableName = "problems")
data class ProblemEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: String, // "Easy", "Medium", "Hard"
    val topic: String,
    val constraints: String,
    val examples: List<Example>,
    val hints: List<String>,
    val editorial: String,
    val timeEstimateMinutes: Int,
    val acceptanceRate: Float,
    val xpReward: Int,
    val starterCode: Map<String, String>, // language -> code
    val testCases: List<TestCase>
)

fun ProblemEntity.toDomainModel(): com.dsafun.app.domain.model.Problem {
    return com.dsafun.app.domain.model.Problem(
        id = id,
        title = title,
        description = description,
        difficulty = difficulty,
        topic = topic,
        constraints = constraints,
        examples = examples,
        hints = hints,
        editorial = editorial,
        timeEstimateMinutes = timeEstimateMinutes,
        acceptanceRate = acceptanceRate,
        xpReward = xpReward,
        starterCode = starterCode,
        testCases = testCases
    )
}

// Made with Bob
