package com.ciffelia.volundr

import android.app.Service
import android.content.Intent
import android.media.MediaRouter
import android.os.IBinder


class ForegroundService : Service() {

    private val mediaRouteProvider by lazy { MediaRouteProvider(this) }
    private val notificationGateway by lazy { NotificationGateway(this) }

    override fun onBind(intent: Intent): IBinder? = TODO()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationGateway.startForeground()

        mediaRouteProvider.apply {
            audioDeviceChangeHandler = ::displayAlertIfNeeded
            volumeChangeHandler = ::displayAlertIfNeeded
        }

        displayAlertIfNeeded(mediaRouteProvider.selectedRoute)

        return START_STICKY
    }

    private fun displayAlertIfNeeded(selectedRoute: MediaRouter.RouteInfo) {
        if (shouldDisplayAlert(selectedRoute)) {
            notificationGateway.displayAlertNotification()
        } else {
            notificationGateway.hideAlertNotification()
        }
    }

    private fun shouldDisplayAlert(selectedRoute: MediaRouter.RouteInfo) = (selectedRoute.name == "Phone" && selectedRoute.volume > 0)
}
