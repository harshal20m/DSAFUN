package com.dsafun.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.domain.usecase.AnalyticsData
import com.dsafun.app.ui.components.charts.BarChart
import com.dsafun.app.ui.components.charts.DonutChart
import com.dsafun.app.ui.components.charts.HeatmapCalendar
import com.dsafun.app.ui.components.charts.LineChart
import com.dsafun.app.ui.viewmodel.AnalyticsUiState
import com.dsafun.app.ui.viewmodel.AnalyticsViewModel
import com.dsafun.app.ui.viewmodel.TimeRange

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTimeRange by viewModel.selectedTimeRange.collectAsState()

    when (val state = uiState) {
        is AnalyticsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is AnalyticsUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is AnalyticsUiState.Success -> {
            AnalyticsContent(
                data = state.data,
                selectedTimeRange = selectedTimeRange,
                onTimeRangeSelected = { viewModel.selectTimeRange(it) },
                getFilteredData = { viewModel.getFilteredWeeklyData(state.data) }
            )
        }
    }
}

@Composable
private fun AnalyticsContent(
    data: AnalyticsData,
    selectedTimeRange: TimeRange,
    onTimeRangeSelected: (TimeRange) -> Unit,
    getFilteredData: () -> AnalyticsData
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Section 1: Summary Cards
        item {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                item {
                    SummaryCard(
                        title = "Total Solved",
                        value = data.totalSolved.toString(),
                        color = Color(0xFF10B981)
                    )
                }
                item {
                    SummaryCard(
                        title = "Current Streak",
                        value = "${data.currentStreak} days",
                        color = Color(0xFF3B82F6)
                    )
                }
                item {
                    SummaryCard(
                        title = "Best Streak",
                        value = "${data.bestStreak} days",
                        color = Color(0xFFF59E0B)
                    )
                }
                item {
                    SummaryCard(
                        title = "Accuracy",
                        value = "${data.accuracyRate}%",
                        color = Color(0xFFEF4444)
                    )
                }
            }
        }

        // Section 2: Heatmap
        item {
            SectionHeader(title = "Your Activity")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                HeatmapCalendar(
                    data = data.heatmapData,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Section 3: Weekly Progress
        item {
            SectionHeader(title = "Weekly Progress")
            
            // Time range chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimeRangeChip(
                    label = "1M",
                    selected = selectedTimeRange == TimeRange.ONE_MONTH,
                    onClick = { onTimeRangeSelected(TimeRange.ONE_MONTH) }
                )
                TimeRangeChip(
                    label = "3M",
                    selected = selectedTimeRange == TimeRange.THREE_MONTHS,
                    onClick = { onTimeRangeSelected(TimeRange.THREE_MONTHS) }
                )
                TimeRangeChip(
                    label = "All",
                    selected = selectedTimeRange == TimeRange.ALL,
                    onClick = { onTimeRangeSelected(TimeRange.ALL) }
                )
            }
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                val filteredData = getFilteredData()
                LineChart(
                    data = filteredData.weeklyProgress,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Section 4: Topics
        item {
            SectionHeader(title = "Topics Distribution")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                DonutChart(
                    data = data.topicDistribution,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Section 5: Difficulty Breakdown
        item {
            SectionHeader(title = "Difficulty Breakdown")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DifficultyCard(
                    title = "Easy",
                    count = data.difficultyBreakdown.easy,
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
                DifficultyCard(
                    title = "Medium",
                    count = data.difficultyBreakdown.medium,
                    color = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )
                DifficultyCard(
                    title = "Hard",
                    count = data.difficultyBreakdown.hard,
                    color = Color(0xFFEF4444),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section 6: Languages
        item {
            SectionHeader(title = "Language Usage")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                BarChart(
                    data = data.languageUsage,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Section 7: Personal Records
        item {
            SectionHeader(title = "Personal Records")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    data.fastestSolves.easy?.let {
                        PersonalRecordItem(
                            difficulty = "Easy",
                            problemName = it.problemName,
                            time = it.timeSeconds,
                            color = Color(0xFF10B981)
                        )
                    }
                    data.fastestSolves.medium?.let {
                        PersonalRecordItem(
                            difficulty = "Medium",
                            problemName = it.problemName,
                            time = it.timeSeconds,
                            color = Color(0xFFF59E0B)
                        )
                    }
                    data.fastestSolves.hard?.let {
                        PersonalRecordItem(
                            difficulty = "Hard",
                            problemName = it.problemName,
                            time = it.timeSeconds,
                            color = Color(0xFFEF4444)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeRangeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) }
    )
}

@Composable
private fun DifficultyCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun PersonalRecordItem(
    difficulty: String,
    problemName: String,
    time: Int,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = difficulty,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = problemName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = color.copy(alpha = 0.1f)
        ) {
            Text(
                text = "${time}s",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

// Made with Bob
