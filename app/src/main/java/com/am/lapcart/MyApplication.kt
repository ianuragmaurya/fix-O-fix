package com.am.lapcart

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics

class MyApplication : Application() {

    companion object {
        lateinit var analytics: FirebaseAnalytics
    }

    override fun onCreate() {
        super.onCreate()

        analytics = FirebaseAnalytics.getInstance(this)
    }
}