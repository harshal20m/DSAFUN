package com.dsafun.app.data.repository

import com.dsafun.app.data.local.dao.BadgeDao
import com.dsafun.app.data.local.entity.BadgeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BadgeRepository @Inject constructor(
    private val badgeDao: BadgeDao
) {
    
    fun getAllBadges(): Flow<List<BadgeEntity>> {
        return badgeDao.getAllBadges()
    }
    
    fun getUnlockedBadges(): Flow<List<BadgeEntity>> {
        return badgeDao.getUnlockedBadges()
    }
    
    suspend fun getBadgeById(badgeId: String): BadgeEntity? {
        return badgeDao.getBadgeById(badgeId)
    }
    
    suspend fun unlockBadge(badgeId: String) {
        val timestamp = System.currentTimeMillis()
        badgeDao.unlockBadge(badgeId, timestamp)
    }
    
    suspend fun getUnlockedCount(): Int {
        return badgeDao.getUnlockedCount()
    }
}

// Made with Bob