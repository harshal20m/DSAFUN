package com.dsafun.app.data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class TestCase(
    val input: String,
    val expectedOutput: String,
    val isHidden: Boolean = false
)

// Made with Bob
