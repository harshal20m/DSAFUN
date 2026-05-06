package com.dsafun.app.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dsafun.app.data.local.DsaDatabase
import com.dsafun.app.data.local.dao.BadgeDao
import com.dsafun.app.data.local.dao.DailyProgressDao
import com.dsafun.app.data.local.dao.FocusSessionDao
import com.dsafun.app.data.local.dao.ProblemDao
import com.dsafun.app.data.local.dao.UserSolutionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create user_solutions table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS user_solutions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    problemId INTEGER NOT NULL,
                    language TEXT NOT NULL,
                    code TEXT NOT NULL,
                    status TEXT NOT NULL,
                    timeTakenSeconds INTEGER NOT NULL,
                    attemptCount INTEGER NOT NULL,
                    solvedAt INTEGER,
                    isFavorite INTEGER NOT NULL,
                    lastEditedAt INTEGER NOT NULL
                )
            """.trimIndent())
        }
    }
    
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create daily_progress table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS daily_progress (
                    date TEXT PRIMARY KEY NOT NULL,
                    problemsSolved INTEGER NOT NULL,
                    goalTarget INTEGER NOT NULL,
                    xpEarned INTEGER NOT NULL,
                    streakActive INTEGER NOT NULL,
                    timeSpentMinutes INTEGER NOT NULL
                )
            """.trimIndent())
        }
    }
    
    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create badges table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS badges (
                    id TEXT PRIMARY KEY NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT NOT NULL,
                    iconType TEXT NOT NULL,
                    isUnlocked INTEGER NOT NULL,
                    unlockedAt INTEGER
                )
            """.trimIndent())
        }
    }
    
    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create focus_sessions table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS focus_sessions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    linkedProblemId INTEGER,
                    durationSeconds INTEGER NOT NULL,
                    sessionType TEXT NOT NULL,
                    completedAt INTEGER NOT NULL
                )
            """.trimIndent())
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DsaDatabase {
        return Room.databaseBuilder(
            context,
            DsaDatabase::class.java,
            "dsa_database"
        )
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
        .build()
    }

    @Provides
    @Singleton
    fun provideProblemDao(database: DsaDatabase): ProblemDao {
        return database.problemDao()
    }

    @Provides
    @Singleton
    fun provideUserSolutionDao(database: DsaDatabase): UserSolutionDao {
        return database.userSolutionDao()
    }
    
    @Provides
    @Singleton
    fun provideDailyProgressDao(database: DsaDatabase): DailyProgressDao {
        return database.dailyProgressDao()
    }
    
    @Provides
    @Singleton
    fun provideBadgeDao(database: DsaDatabase): BadgeDao {
        return database.badgeDao()
    }
    
    @Provides
    @Singleton
    fun provideFocusSessionDao(database: DsaDatabase): FocusSessionDao {
        return database.focusSessionDao()
    }
}

// Made with Bob
