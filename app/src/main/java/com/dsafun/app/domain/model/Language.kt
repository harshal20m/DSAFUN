package com.dsafun.app.domain.model

enum class Language(
    val displayName: String,
    val fileExtension: String,
    val commentPrefix: String
) {
    KOTLIN("Kotlin", "kt", "//"),
    JAVA("Java", "java", "//"),
    PYTHON("Python", "py", "#"),
    JAVASCRIPT("JavaScript", "js", "//"),
    CPP("C++", "cpp", "//");

    companion object {
        fun fromString(value: String): Language {
            return entries.find { it.name == value } ?: KOTLIN
        }
    }
}

// Made with Bob