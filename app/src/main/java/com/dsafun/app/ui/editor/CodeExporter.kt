package com.dsafun.app.ui.editor

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.dsafun.app.domain.model.Language
import com.dsafun.app.domain.model.Problem
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CodeExporter(private val context: Context) {
    
    fun exportCode(
        problem: Problem,
        language: Language,
        code: String,
        timeTaken: Long
    ): Intent? {
        return try {
            val file = createCodeFile(problem, language, code, timeTaken)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "${problem.title} - ${language.displayName}")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun createCodeFile(
        problem: Problem,
        language: Language,
        code: String,
        timeTaken: Long
    ): File {
        val cacheDir = File(context.cacheDir, "exports")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        
        val fileName = "${problem.title.replace(" ", "_")}.${language.fileExtension}"
        val file = File(cacheDir, fileName)
        
        val header = buildHeader(problem, language, timeTaken)
        val fullContent = header + "\n\n" + code
        
        file.writeText(fullContent)
        return file
    }
    
    private fun buildHeader(problem: Problem, language: Language, timeTaken: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val timestamp = dateFormat.format(Date())
        val minutes = timeTaken / 60
        val seconds = timeTaken % 60
        
        return when (language) {
            Language.PYTHON -> """
                # Problem: ${problem.title}
                # Difficulty: ${problem.difficulty}
                # Topic: ${problem.topic}
                # Time Taken: ${minutes}m ${seconds}s
                # Exported: $timestamp
            """.trimIndent()
            else -> """
                ${language.commentPrefix} Problem: ${problem.title}
                ${language.commentPrefix} Difficulty: ${problem.difficulty}
                ${language.commentPrefix} Topic: ${problem.topic}
                ${language.commentPrefix} Time Taken: ${minutes}m ${seconds}s
                ${language.commentPrefix} Exported: $timestamp
            """.trimIndent()
        }
    }
}

// Made with Bob