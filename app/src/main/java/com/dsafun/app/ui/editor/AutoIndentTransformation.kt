package com.dsafun.app.ui.editor

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping

/**
 * Auto-indent transformation for code editor
 * - On Enter: copies current line indent
 * - After : or { → adds one indent level
 * - Tab key → converts to 4 spaces
 */
object AutoIndentTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = text,
            offsetMapping = OffsetMapping.Identity
        )
    }
}

/**
 * Handles auto-indent logic when text changes
 * Call this in your onValueChange handler
 */
fun handleAutoIndent(
    oldValue: TextFieldValue,
    newValue: TextFieldValue
): TextFieldValue {
    // Check if user pressed Enter
    if (newValue.text.length > oldValue.text.length && 
        newValue.text.getOrNull(newValue.selection.start - 1) == '\n') {
        
        val cursorPosition = newValue.selection.start
        val textBeforeCursor = newValue.text.substring(0, cursorPosition)
        val lines = textBeforeCursor.split('\n')
        
        if (lines.size >= 2) {
            val previousLine = lines[lines.size - 2]
            val indent = getIndent(previousLine)
            
            // Check if previous line ends with : or {
            val trimmedPrevious = previousLine.trimEnd()
            val extraIndent = if (trimmedPrevious.endsWith(':') || 
                                   trimmedPrevious.endsWith('{')) {
                "    " // 4 spaces
            } else {
                ""
            }
            
            val indentToAdd = indent + extraIndent
            
            if (indentToAdd.isNotEmpty()) {
                val newText = StringBuilder(newValue.text)
                    .insert(cursorPosition, indentToAdd)
                    .toString()
                
                return TextFieldValue(
                    text = newText,
                    selection = androidx.compose.ui.text.TextRange(
                        cursorPosition + indentToAdd.length
                    )
                )
            }
        }
    }
    
    // Handle Tab key → convert to 4 spaces
    if (newValue.text.length > oldValue.text.length &&
        newValue.text.getOrNull(newValue.selection.start - 1) == '\t') {
        
        val cursorPosition = newValue.selection.start
        val newText = newValue.text.replaceRange(
            cursorPosition - 1,
            cursorPosition,
            "    " // Replace tab with 4 spaces
        )
        
        return TextFieldValue(
            text = newText,
            selection = androidx.compose.ui.text.TextRange(
                cursorPosition + 3 // Move cursor after 4 spaces (already moved 1)
            )
        )
    }
    
    return newValue
}

/**
 * Extracts the leading whitespace from a line
 */
private fun getIndent(line: String): String {
    val indent = StringBuilder()
    for (char in line) {
        if (char == ' ' || char == '\t') {
            indent.append(char)
        } else {
            break
        }
    }
    return indent.toString()
}

/**
 * Adds indent to selected lines
 */
fun indentSelection(value: TextFieldValue): TextFieldValue {
    val text = value.text
    val selection = value.selection
    
    // Find start and end of lines containing selection
    val startLine = text.substring(0, selection.start).lastIndexOf('\n') + 1
    val endLine = text.indexOf('\n', selection.end).let { 
        if (it == -1) text.length else it 
    }
    
    val selectedText = text.substring(startLine, endLine)
    val lines = selectedText.split('\n')
    val indentedLines = lines.map { "    $it" }
    val indentedText = indentedLines.joinToString("\n")
    
    val newText = text.replaceRange(startLine, endLine, indentedText)
    val indentCount = lines.size * 4
    
    return TextFieldValue(
        text = newText,
        selection = androidx.compose.ui.text.TextRange(
            selection.start + 4,
            selection.end + indentCount
        )
    )
}

/**
 * Removes indent from selected lines
 */
fun unindentSelection(value: TextFieldValue): TextFieldValue {
    val text = value.text
    val selection = value.selection
    
    // Find start and end of lines containing selection
    val startLine = text.substring(0, selection.start).lastIndexOf('\n') + 1
    val endLine = text.indexOf('\n', selection.end).let { 
        if (it == -1) text.length else it 
    }
    
    val selectedText = text.substring(startLine, endLine)
    val lines = selectedText.split('\n')
    val unindentedLines = lines.map { line ->
        when {
            line.startsWith("    ") -> line.substring(4)
            line.startsWith("\t") -> line.substring(1)
            else -> line
        }
    }
    val unindentedText = unindentedLines.joinToString("\n")
    
    val removedChars = selectedText.length - unindentedText.length
    val newText = text.replaceRange(startLine, endLine, unindentedText)
    
    return TextFieldValue(
        text = newText,
        selection = androidx.compose.ui.text.TextRange(
            maxOf(startLine, selection.start - 4),
            maxOf(startLine, selection.end - removedChars)
        )
    )
}

// Made with Bob
