package com.dsafun.app

import android.app.Application
import com.dsafun.app.data.local.DatabaseSeeder
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import com.dsafun.app.notifications.NotificationChannels
import com.dsafun.app.workers.WorkerScheduler
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class DsaApp : Application() {
    
    @Inject
    lateinit var databaseSeeder: DatabaseSeeder
    
    @Inject
    lateinit var workerScheduler: WorkerScheduler
    
    @Inject
    lateinit var userPreferences: UserPreferencesDataStore
    
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    override fun onCreate() {
        super.onCreate()
        
        // Create notification channels
        NotificationChannels.createChannels(this)
        
        // Seed database on first launch
        applicationScope.launch {
            databaseSeeder.seedDatabase()
        }
        
        // Schedule workers for notifications
        applicationScope.launch {
            val reminderEnabled = userPreferences.reminderEnabled.first()
            if (reminderEnabled) {
                val reminderHour = userPreferences.reminderHour.first()
                val reminderMinute = userPreferences.reminderMinute.first()
                workerScheduler.scheduleAll(reminderHour, reminderMinute)
            }
        }
    }
}

// Made with Bob
