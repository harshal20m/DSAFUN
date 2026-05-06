package com.dsafun.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dsafun.app.data.local.dao.BadgeDao
import com.dsafun.app.data.local.dao.DailyProgressDao
import com.dsafun.app.data.local.dao.FocusSessionDao
import com.dsafun.app.data.local.dao.ProblemDao
import com.dsafun.app.data.local.dao.UserSolutionDao
import com.dsafun.app.data.local.entity.BadgeEntity
import com.dsafun.app.data.local.entity.DailyProgressEntity
import com.dsafun.app.data.local.entity.FocusSessionEntity
import com.dsafun.app.data.local.entity.ProblemEntity
import com.dsafun.app.data.local.entity.UserSolutionEntity

@Database(
    entities = [
        ProblemEntity::class,
        UserSolutionEntity::class,
        DailyProgressEntity::class,
        BadgeEntity::class,
        FocusSessionEntity::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DsaDatabase : RoomDatabase() {
    abstract fun problemDao(): ProblemDao
    abstract fun userSolutionDao(): UserSolutionDao
    abstract fun dailyProgressDao(): DailyProgressDao
    abstract fun badgeDao(): BadgeDao
    abstract fun focusSessionDao(): FocusSessionDao
}

// Made with Bob
