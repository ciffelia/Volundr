package com.ciffelia.volundr

import android.content.Context
import androidx.mediarouter.media.MediaRouter
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaControlIntent


class MediaRouteProvider(context: Context) {

    var audioDeviceChangeHandler: ((MediaRouter.RouteInfo) -> Unit)? = null
    var volumeChangeHandler: ((MediaRouter.RouteInfo) -> Unit)? = null


    private val mediaRouter = MediaRouter.getInstance(context)

    private val mediaRouteSelector = MediaRouteSelector.Builder()
            .addControlCategory(MediaControlIntent.CATEGORY_LIVE_AUDIO)
            .addControlCategory(MediaControlIntent.CATEGORY_LIVE_VIDEO)
            .build()

    val selectedRoute: MediaRouter.RouteInfo
        get() = mediaRouter.selectedRoute

    private val mediaRouterCallback = object: MediaRouter.Callback() {
        override fun onRouteSelected(router: MediaRouter, info: MediaRouter.RouteInfo) {
            audioDeviceChangeHandler?.invoke(selectedRoute)
        }
        override fun onRouteVolumeChanged(router: MediaRouter, info: MediaRouter.RouteInfo) {
            volumeChangeHandler?.invoke(selectedRoute)
        }
    }

    init {
        mediaRouter.addCallback(mediaRouteSelector, mediaRouterCallback)
    }
}
