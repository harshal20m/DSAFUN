package com.dsafun.app.data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class Example(
    val input: String,
    val output: String,
    val explanation: String? = null
)

// Made with Bob
