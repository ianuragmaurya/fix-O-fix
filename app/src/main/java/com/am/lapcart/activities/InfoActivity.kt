package com.am.lapcart.activities

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.am.lapcart.R

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()

        val page = intent.getStringExtra("page")

        when (page) {
            "about" -> webView.loadUrl("file:///android_asset/about.html")
            "privacy" -> webView.loadUrl("file:///android_asset/privacy_policy.html")
            "terms" -> webView.loadUrl("file:///android_asset/terms.html")
            "help" -> webView.loadUrl("file:///android_asset/help.html")
        }
    }
}