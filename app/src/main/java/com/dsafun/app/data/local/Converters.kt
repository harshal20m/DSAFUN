package com.dsafun.app.data.local

import androidx.room.TypeConverter
import com.dsafun.app.data.local.model.Example
import com.dsafun.app.data.local.model.TestCase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromExampleList(value: List<Example>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toExampleList(value: String): List<Example> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromTestCaseList(value: List<TestCase>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toTestCaseList(value: String): List<TestCase> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromStringMap(value: Map<String, String>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toStringMap(value: String): Map<String, String> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return json.decodeFromString(value)
    }
}

// Made with Bob
