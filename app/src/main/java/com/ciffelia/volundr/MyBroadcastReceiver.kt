package com.ciffelia.volundr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_MY_PACKAGE_REPLACED, Intent.ACTION_BOOT_COMPLETED -> startService(context)
            "ALERT_NOTIFICATION_CLICKED" -> setVolumeZero(context)
        }
    }

    private fun startService(context: Context) {
        val serviceIntent = Intent(context, ForegroundService::class.java)
        context.startForegroundService(serviceIntent)
    }

    private fun setVolumeZero(context: Context) {
        val mediaRouteProvider = MediaRouteProvider(context)
        mediaRouteProvider.selectedRoute.requestSetVolume(0)
    }
}
