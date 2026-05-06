package com.dsafun.app.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dsafun.app.workers.WorkerScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var workerScheduler: WorkerScheduler

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule all workers after device reboot
            scope.launch {
                workerScheduler.scheduleAll()
            }
        }
    }
}

// Made with Bob