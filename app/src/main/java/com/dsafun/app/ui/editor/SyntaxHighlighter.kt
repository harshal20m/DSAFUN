package com.dsafun.app.ui.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.dsafun.app.domain.model.Language

class SyntaxHighlighter(
    private val language: Language,
    private val keywordColor: Color,
    private val stringColor: Color,
    private val commentColor: Color,
    private val numberColor: Color
) {
    // Cache for highlighted code to avoid re-processing
    private val cache = mutableMapOf<String, AnnotatedString>()
    private val maxCacheSize = 50 // Limit cache size to prevent memory issues
    
    private val keywords = when (language) {
        Language.KOTLIN -> setOf(
            "abstract", "actual", "annotation", "as", "break", "by", "catch", "class",
            "companion", "const", "constructor", "continue", "crossinline", "data",
            "delegate", "do", "dynamic", "else", "enum", "expect", "external", "false",
            "field", "file", "final", "finally", "for", "fun", "get", "if", "import",
            "in", "infix", "init", "inline", "inner", "interface", "internal", "is",
            "lateinit", "noinline", "null", "object", "open", "operator", "out",
            "override", "package", "param", "private", "property", "protected", "public",
            "receiver", "reified", "return", "sealed", "set", "setparam", "super",
            "suspend", "tailrec", "this", "throw", "true", "try", "typealias", "typeof",
            "val", "var", "vararg", "when", "where", "while"
        )
        Language.JAVA -> setOf(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
            "class", "const", "continue", "default", "do", "double", "else", "enum",
            "extends", "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new", "null",
            "package", "private", "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while", "true", "false"
        )
        Language.PYTHON -> setOf(
            "False", "None", "True", "and", "as", "assert", "async", "await", "break",
            "class", "continue", "def", "del", "elif", "else", "except", "finally",
            "for", "from", "global", "if", "import", "in", "is", "lambda", "nonlocal",
            "not", "or", "pass", "raise", "return", "try", "while", "with", "yield"
        )
        Language.JAVASCRIPT -> setOf(
            "abstract", "arguments", "await", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "debugger", "default", "delete", "do",
            "double", "else", "enum", "eval", "export", "extends", "false", "final",
            "finally", "float", "for", "function", "goto", "if", "implements", "import",
            "in", "instanceof", "int", "interface", "let", "long", "native", "new",
            "null", "package", "private", "protected", "public", "return", "short",
            "static", "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "true", "try", "typeof", "var", "void", "volatile", "while",
            "with", "yield"
        )
        Language.CPP -> setOf(
            "alignas", "alignof", "and", "and_eq", "asm", "auto", "bitand", "bitor",
            "bool", "break", "case", "catch", "char", "char16_t", "char32_t", "class",
            "compl", "const", "constexpr", "const_cast", "continue", "decltype", "default",
            "delete", "do", "double", "dynamic_cast", "else", "enum", "explicit", "export",
            "extern", "false", "float", "for", "friend", "goto", "if", "inline", "int",
            "long", "mutable", "namespace", "new", "noexcept", "not", "not_eq", "nullptr",
            "operator", "or", "or_eq", "private", "protected", "public", "register",
            "reinterpret_cast", "return", "short", "signed", "sizeof", "static",
            "static_assert", "static_cast", "struct", "switch", "template", "this",
            "thread_local", "throw", "true", "try", "typedef", "typeid", "typename",
            "union", "unsigned", "using", "virtual", "void", "volatile", "wchar_t",
            "while", "xor", "xor_eq"
        )
    }

    fun highlight(code: String): AnnotatedString {
        // Check cache first
        cache[code]?.let { return it }
        
        // If cache is too large, clear oldest entries (simple FIFO)
        if (cache.size >= maxCacheSize) {
            val keysToRemove = cache.keys.take(10)
            keysToRemove.forEach { cache.remove(it) }
        }
        
        // Build and cache the result
        val result = buildAnnotatedString {
            append(code)
            
            // Highlight comments
            highlightComments(code)
            
            // Highlight strings
            highlightStrings(code)
            
            // Highlight numbers
            highlightNumbers(code)
            
            // Highlight keywords
            highlightKeywords(code)
        }
        
        cache[code] = result
        return result
    }
    
    fun clearCache() {
        cache.clear()
    }

    private fun AnnotatedString.Builder.highlightComments(code: String) {
        val commentPattern = when (language) {
            Language.PYTHON -> Regex("#.*")
            else -> Regex("//.*|/\\*[\\s\\S]*?\\*/")
        }
        
        commentPattern.findAll(code).forEach { match ->
            addStyle(
                style = SpanStyle(color = commentColor),
                start = match.range.first,
                end = match.range.last + 1
            )
        }
    }

    private fun AnnotatedString.Builder.highlightStrings(code: String) {
        val stringPattern = Regex("\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'")
        
        stringPattern.findAll(code).forEach { match ->
            addStyle(
                style = SpanStyle(color = stringColor),
                start = match.range.first,
                end = match.range.last + 1
            )
        }
    }

    private fun AnnotatedString.Builder.highlightNumbers(code: String) {
        val numberPattern = Regex("\\b\\d+(\\.\\d+)?\\b")
        
        numberPattern.findAll(code).forEach { match ->
            addStyle(
                style = SpanStyle(color = numberColor),
                start = match.range.first,
                end = match.range.last + 1
            )
        }
    }

    private fun AnnotatedString.Builder.highlightKeywords(code: String) {
        keywords.forEach { keyword ->
            val pattern = Regex("\\b$keyword\\b")
            pattern.findAll(code).forEach { match ->
                addStyle(
                    style = SpanStyle(color = keywordColor),
                    start = match.range.first,
                    end = match.range.last + 1
                )
            }
        }
    }
}

// Made with Bob