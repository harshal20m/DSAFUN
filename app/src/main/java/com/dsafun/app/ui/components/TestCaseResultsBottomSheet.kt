package com.dsafun.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dsafun.app.domain.executor.TestCaseResult
import com.dsafun.app.ui.theme.Dimens

/**
 * Bottom sheet showing test case results
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestCaseResultsBottomSheet(
    testResults: List<TestCaseResult>,
    isRunning: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Test Results",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                
                if (!isRunning) {
                    val passedCount = testResults.count { it.passed }
                    val totalCount = testResults.size
                    Text(
                        text = "$passedCount/$totalCount Passed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (passedCount == totalCount) 
                            Color(0xFF10B981) else Color(0xFFF59E0B)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
            
            // Disclaimer
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "⚠️ Results are simulated. Run locally for exact output.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(Dimens.SpacingSmall)
                )
            }
            
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
            
            // Test Results List
            if (isRunning) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                        Text(
                            text = "Running tests...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                ) {
                    items(testResults) { result ->
                        TestCaseResultItem(result = result)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}

@Composable
private fun TestCaseResultItem(
    result: TestCaseResult,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (result.passed) 
                Color(0xFF10B981).copy(alpha = 0.1f)
            else 
                Color(0xFFEF4444).copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium),
            verticalAlignment = Alignment.Top
        ) {
            // Status Icon
            Icon(
                imageVector = if (result.passed) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = null,
                tint = if (result.passed) Color(0xFF10B981) else Color(0xFFEF4444),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(Dimens.SpacingMedium))
            
            // Test Details
            Column(modifier = Modifier.weight(1f)) {
                // Input
                Text(
                    text = "Input:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = result.input,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                
                // Expected Output
                Text(
                    text = "Expected:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = result.expectedOutput,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                
                // Actual Output
                Text(
                    text = "Actual:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = result.actualOutput,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace
                    ),
                    color = if (result.passed) Color(0xFF10B981) else Color(0xFFEF4444)
                )
                
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                
                // Console Output (if any)
                if (result.consoleOutput.isNotEmpty()) {
                    Text(
                        text = "Console:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = result.consoleOutput,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                }
                
                // Error Message (if any)
                result.errorMessage?.let { error ->
                    Text(
                        text = "Error:",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFEF4444)
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFEF4444).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace
                            ),
                            color = Color(0xFFEF4444),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                }
                
                // Execution Time
                Text(
                    text = "Time: ${result.executionTimeMs}ms",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Made with Bob
