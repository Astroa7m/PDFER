package com.example

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color

/**
* Creating notification channel
*/

const val CHANNEL_ID = "channelId"
private const val CHANNEL_NAME = "Worker Channel"
private const val CHANNEL_DESCRIPTION = "notify users about downloading status"

class Pdfer : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotification()
    }

    private fun createNotification(){
        //Note: notification channels are only required for Android >=26
        // current minSdk is 26 so no need to check the sdk version
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CHANNEL_DESCRIPTION
            enableLights(true)
            lightColor = Color.RED
        }

        with(getSystemService(NOTIFICATION_SERVICE) as NotificationManager){
            createNotificationChannel(channel)
        }
    }

}