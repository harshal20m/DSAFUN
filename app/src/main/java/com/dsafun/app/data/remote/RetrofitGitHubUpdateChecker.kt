package com.dsafun.app.data.remote

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private val Context.updateDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_updates")

@Singleton
class RetrofitGitHubUpdateChecker @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val GITHUB_OWNER = "harshal20m"
    private val GITHUB_REPO = "NotesVault"
    private val CURRENT_VERSION = "1.0.0" // Should match versionName in build.gradle
    
    private val LATEST_VERSION_KEY = stringPreferencesKey("latest_version")
    private val DOWNLOAD_URL_KEY = stringPreferencesKey("download_url")
    private val RELEASE_NOTES_KEY = stringPreferencesKey("release_notes")
    private val IS_DISMISSED_KEY = booleanPreferencesKey("is_dismissed")
    private val DISMISSED_VERSION_KEY = stringPreferencesKey("dismissed_version")
    
    private val apiService: GitHubApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)
    }
    
    suspend fun checkForUpdates(): UpdateInfo? {
        return try {
            val release = apiService.getLatestRelease(GITHUB_OWNER, GITHUB_REPO)
            val latestVersion = release.tag_name.removePrefix("v")
            
            if (isNewerVersion(latestVersion, CURRENT_VERSION)) {
                val apkAsset = release.assets.firstOrNull { it.name.endsWith(".apk") }
                val downloadUrl = apkAsset?.browser_download_url ?: release.html_url
                
                // Check if this version was dismissed
                val dismissedVersion = context.updateDataStore.data.first()[DISMISSED_VERSION_KEY]
                val isDismissed = dismissedVersion == latestVersion
                
                val updateInfo = UpdateInfo(
                    latestVersion = latestVersion,
                    currentVersion = CURRENT_VERSION,
                    downloadUrl = downloadUrl,
                    releaseNotes = release.body,
                    isDismissed = isDismissed
                )
                
                // Save update info
                saveUpdateInfo(updateInfo)
                
                updateInfo
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun isNewerVersion(latest: String, current: String): Boolean {
        val latestParts = latest.split(".").map { it.toIntOrNull() ?: 0 }
        val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }
        
        for (i in 0 until maxOf(latestParts.size, currentParts.size)) {
            val latestPart = latestParts.getOrNull(i) ?: 0
            val currentPart = currentParts.getOrNull(i) ?: 0
            
            if (latestPart > currentPart) return true
            if (latestPart < currentPart) return false
        }
        
        return false
    }
    
    private suspend fun saveUpdateInfo(updateInfo: UpdateInfo) {
        context.updateDataStore.edit { prefs ->
            prefs[LATEST_VERSION_KEY] = updateInfo.latestVersion
            prefs[DOWNLOAD_URL_KEY] = updateInfo.downloadUrl
            prefs[RELEASE_NOTES_KEY] = updateInfo.releaseNotes
        }
    }
    
    suspend fun dismissUpdate(version: String) {
        context.updateDataStore.edit { prefs ->
            prefs[IS_DISMISSED_KEY] = true
            prefs[DISMISSED_VERSION_KEY] = version
        }
    }
    
    fun getUpdateInfo(): Flow<UpdateInfo?> {
        return context.updateDataStore.data.map { prefs ->
            val latestVersion = prefs[LATEST_VERSION_KEY]
            val downloadUrl = prefs[DOWNLOAD_URL_KEY]
            val releaseNotes = prefs[RELEASE_NOTES_KEY]
            val dismissedVersion = prefs[DISMISSED_VERSION_KEY]
            
            if (latestVersion != null && downloadUrl != null) {
                UpdateInfo(
                    latestVersion = latestVersion,
                    currentVersion = CURRENT_VERSION,
                    downloadUrl = downloadUrl,
                    releaseNotes = releaseNotes ?: "",
                    isDismissed = dismissedVersion == latestVersion
                )
            } else {
                null
            }
        }
    }
}

// Made with Bob
