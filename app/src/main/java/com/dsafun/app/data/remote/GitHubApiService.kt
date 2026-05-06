package com.dsafun.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

data class GitHubRelease(
    val tag_name: String,
    val name: String,
    val body: String,
    val html_url: String,
    val published_at: String,
    val assets: List<GitHubAsset>
)

data class GitHubAsset(
    val name: String,
    val browser_download_url: String,
    val size: Long
)

data class UpdateInfo(
    val latestVersion: String,
    val currentVersion: String,
    val downloadUrl: String,
    val releaseNotes: String,
    val isDismissed: Boolean = false
)

interface GitHubApiService {
    @GET("repos/{owner}/{repo}/releases/latest")
    suspend fun getLatestRelease(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): GitHubRelease
}

// Made with Bob
