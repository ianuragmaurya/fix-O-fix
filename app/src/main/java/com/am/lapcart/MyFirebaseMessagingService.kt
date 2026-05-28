package com.am.lapcart

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
 import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("FCM Token : $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "Notification"
        val massage = remoteMessage.notification?.body ?: "New Message"

        showNotification(title, massage)
    }

    private fun showNotification(title: String, massage : String) {
         val channelId = "default_channel"


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(channelId, "default_channel", NotificationManager.IMPORTANCE_HIGH)

            val manager = getSystemService(NotificationManager::class.java)

            manager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(massage)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        val manager = NotificationManagerCompat.from(this)
        manager.notify(
            1,
            notification
        )
    }
}