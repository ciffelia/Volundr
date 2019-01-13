package com.ciffelia.volundr

import android.app.*
import android.content.Context
import android.content.Intent


class NotificationGateway(private val service: Service) {

    companion object {
        private const val foregroundChannelId = "volundr_foreground"
        private const val alertChannelId = "volundr_alert"
        private const val foregroundNotificationId = 1
        private const val alertNotificationId = 2
    }

    private val notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val foregroundNotification = run {
        Notification.Builder(service, foregroundChannelId).apply {
            setContentTitle("Volundr")
            setContentText("Running")
            setCategory(Notification.CATEGORY_SERVICE)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setOngoing(true)
        }.build()
    }

    private val alertNotification = run {
        val intent = Intent(service, MyBroadcastReceiver::class.java).apply {
            action = "ALERT_NOTIFICATION_CLICKED"
        }

        Notification.Builder(service, alertChannelId).apply {
            setContentTitle("Built-in speaker is unmuted")
            setContentText("Tap to mute")
            setCategory(Notification.CATEGORY_STATUS)
            setSmallIcon(R.drawable.ic_volume_up_white_24dp)
            setOngoing(true)
            setContentIntent(PendingIntent.getBroadcast(service, 0 , intent, 0))
        }.build()
    }


    init {
        createForegroundChannelIfNeeded()
        createAlertChannelIfNeeded()
    }


    fun startForeground() = service.startForeground(foregroundNotificationId, foregroundNotification)

    fun displayAlertNotification() = notificationManager.notify(alertNotificationId, alertNotification)
    fun hideAlertNotification() = notificationManager.cancel(alertNotificationId)


    private fun createForegroundChannelIfNeeded() {
        val name = "Volundr Foreground"
        val description = "Notify when volundr is running"
        createNotificationChannelIfNeeded(foregroundChannelId, name, description, NotificationManager.IMPORTANCE_NONE)
    }

    private fun createAlertChannelIfNeeded() {
        val name = "Volundr Volume Alert"
        val description = "Notify when built-in speaker is unmuted"
        createNotificationChannelIfNeeded(alertChannelId, name, description, NotificationManager.IMPORTANCE_LOW)
    }

    private fun notificationChannelExists(id: String) = notificationManager.getNotificationChannel(id) != null

    private fun createNotificationChannelIfNeeded(id: String, name: String, channelDescription: String, importance: Int) {
        if (notificationChannelExists(id)) return

        val channel = NotificationChannel(id, name, importance)
        channel.apply {
            description = channelDescription
            enableLights(false)
            enableVibration(false)
            setShowBadge(false)
        }
        notificationManager.createNotificationChannel(channel)
    }
}
