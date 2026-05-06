package com.dsafun.app.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.dsafun.app.MainActivity
import com.dsafun.app.data.local.dao.DailyProgressDao
import com.dsafun.app.data.local.dao.ProblemDao
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun problemDao(): ProblemDao
    fun dailyProgressDao(): DailyProgressDao
    fun dataStore(): UserPreferencesDataStore
}

class DsaWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        )
        
        val problemDao = entryPoint.problemDao()
        val dailyProgressDao = entryPoint.dailyProgressDao()
        val dataStore = entryPoint.dataStore()
        
        // Fetch data
        val currentStreak = dataStore.currentStreak.first()
        val dailyGoal = dataStore.dailyGoal.first()
        val today = LocalDate.now().toString()
        val todayProgress = dailyProgressDao.getProgressForDate(today).first()
        val problemsSolvedToday = todayProgress?.problemsSolved ?: 0
        
        // Get today's challenge (first problem)
        val allProblems = problemDao.getAllProblems().first()
        val todayChallenge = allProblems.firstOrNull()
        
        provideContent {
            WidgetContent(
                streak = currentStreak,
                problemsSolvedToday = problemsSolvedToday,
                dailyGoal = dailyGoal,
                challengeTitle = todayChallenge?.title ?: "No problems available",
                challengeId = todayChallenge?.id?.toLong() ?: 0L,
                context = context
            )
        }
    }
}

@Composable
fun WidgetContent(
    streak: Int,
    problemsSolvedToday: Int,
    dailyGoal: Int,
    challengeTitle: String,
    challengeId: Long,
    context: Context
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(androidx.compose.ui.graphics.Color(0xFF1E1E1E)))
            .padding(12.dp),
        verticalAlignment = Alignment.Vertical.Top,
        horizontalAlignment = Alignment.Horizontal.Start
    ) {
        // Header Row
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Horizontal.Start,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            // App Title
            Text(
                text = "DSA Fun",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(androidx.compose.ui.graphics.Color.White)
                )
            )
            
            Spacer(modifier = GlanceModifier.defaultWeight())
            
            // Streak Badge
            Text(
                text = "🔥 $streak",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(androidx.compose.ui.graphics.Color(0xFFFF9800))
                )
            )
        }
        
        Spacer(modifier = GlanceModifier.height(8.dp))
        
        // Progress Section
        Column(
            modifier = GlanceModifier.fillMaxWidth()
        ) {
            Text(
                text = "Today: $problemsSolvedToday / $dailyGoal",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = ColorProvider(androidx.compose.ui.graphics.Color.White)
                )
            )
        }
        
        Spacer(modifier = GlanceModifier.height(8.dp))
        
        // Daily Challenge Section
        Column(
            modifier = GlanceModifier.fillMaxWidth()
        ) {
            Text(
                text = "Challenge:",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = ColorProvider(androidx.compose.ui.graphics.Color(0xFFB0B0B0))
                )
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            Text(
                text = challengeTitle,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ColorProvider(androidx.compose.ui.graphics.Color.White)
                ),
                maxLines = 2
            )
        }
        
        Spacer(modifier = GlanceModifier.defaultWeight())
        
        // Solve Now Button
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(ColorProvider(androidx.compose.ui.graphics.Color(0xFF6200EE)))
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .clickable {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        action = Intent.ACTION_VIEW
                        data = android.net.Uri.parse("problem://id/$challengeId")
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    context.startActivity(intent)
                },
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Text(
                text = "Solve Now",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(androidx.compose.ui.graphics.Color.White)
                )
            )
        }
    }
}

// Made with Bob