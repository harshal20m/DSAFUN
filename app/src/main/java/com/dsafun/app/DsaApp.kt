package com.dsafun.app

import android.app.Application
import com.dsafun.app.data.local.DatabaseSeeder
import com.dsafun.app.notifications.NotificationChannels
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class DsaApp : Application() {
    
    @Inject
    lateinit var databaseSeeder: DatabaseSeeder
    
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    override fun onCreate() {
        super.onCreate()
        
        // Create notification channels
        NotificationChannels.createChannels(this)
        
        // Seed database on first launch
        applicationScope.launch {
            databaseSeeder.seedDatabase()
        }
    }
}

// Made with Bob
