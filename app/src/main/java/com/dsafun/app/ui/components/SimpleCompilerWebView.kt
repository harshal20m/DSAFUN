package com.dsafun.app.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.dsafun.app.domain.model.Language

/**
 * Simple WebView that just loads OneCompiler
 * No scraping, no injection, just a plain browser
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SimpleCompilerWebView(
    language: Language,
    modifier: Modifier = Modifier
) {
    var isPageLoaded by remember { mutableStateOf(false) }
    var loadingProgress by remember { mutableStateOf(0) }
    var hasLoadError by remember { mutableStateOf(false) }
    
    // Get OneCompiler URL for the language
    val compilerUrl = remember(language) {
        when (language) {
            Language.KOTLIN -> "https://onecompiler.com/kotlin"
            Language.JAVA -> "https://onecompiler.com/java"
            Language.PYTHON -> "https://onecompiler.com/python"
            Language.JAVASCRIPT -> "https://onecompiler.com/javascript"
            Language.CPP -> "https://onecompiler.com/cpp"
        }
    }
    
    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                    setBackgroundColor(android.graphics.Color.WHITE)

                    CookieManager.getInstance().setAcceptCookie(true)
                    CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        javaScriptCanOpenWindowsAutomatically = true
                        loadsImagesAutomatically = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = true
                        displayZoomControls = false
                        setSupportZoom(true)
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        cacheMode = WebSettings.LOAD_DEFAULT
                        databaseEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        allowFileAccess = true
                        allowContentAccess = true
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isPageLoaded = false
                            hasLoadError = false
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isPageLoaded = true
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean = false
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            loadingProgress = newProgress
                        }
                    }

                    scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
                    isVerticalScrollBarEnabled = true
                    isHorizontalScrollBarEnabled = true

                    loadUrl(compilerUrl)
                }
            },
            update = { webView ->
                if (webView.url != compilerUrl) {
                    webView.loadUrl(compilerUrl)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Loading indicator
        if (!isPageLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Loading Compiler... $loadingProgress%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (loadingProgress > 0) {
                        LinearProgressIndicator(
                            progress = loadingProgress / 100f,
                            modifier = Modifier.width(200.dp)
                        )
                    }
                }
            }
        }
    }
}

// Made with Bob