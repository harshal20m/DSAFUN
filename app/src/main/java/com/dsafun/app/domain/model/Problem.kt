package com.dsafun.app.domain.model

import com.dsafun.app.data.local.model.Example
import com.dsafun.app.data.local.model.TestCase

data class Problem(
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: String,
    val topic: String,
    val constraints: String,
    val examples: List<Example>,
    val hints: List<String>,
    val editorial: String,
    val timeEstimateMinutes: Int,
    val acceptanceRate: Float,
    val xpReward: Int,
    val starterCode: Map<String, String>,
    val testCases: List<TestCase>
)

// Made with Bob
