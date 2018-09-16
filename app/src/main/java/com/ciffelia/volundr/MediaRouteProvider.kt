package com.ciffelia.volundr

import android.content.Context
import android.media.MediaRouter


class MediaRouteProvider(context: Context) {

    var audioDeviceChangeHandler: ((MediaRouter.RouteInfo) -> Unit)? = null
    var volumeChangeHandler: ((MediaRouter.RouteInfo) -> Unit)? = null


    private val mediaRouter = context.getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter

    val selectedRoute: MediaRouter.RouteInfo
        get() = mediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_AUDIO)

    private val mediaRouterCallback = object: MediaRouter.Callback() {
        override fun onRouteSelected(router: MediaRouter, type: Int, info: MediaRouter.RouteInfo) {
            audioDeviceChangeHandler?.invoke(info)
        }
        override fun onRouteVolumeChanged(router: MediaRouter, info: MediaRouter.RouteInfo) {
            volumeChangeHandler?.invoke(selectedRoute)
        }

        override fun onRouteChanged(router: MediaRouter, info: MediaRouter.RouteInfo) {}
        override fun onRouteUnselected(router: MediaRouter, type: Int, info: MediaRouter.RouteInfo) {}
        override fun onRouteAdded(router: MediaRouter, info: MediaRouter.RouteInfo) {}
        override fun onRouteGrouped(router: MediaRouter, info: MediaRouter.RouteInfo, group: MediaRouter.RouteGroup, index: Int) {}
        override fun onRouteRemoved(router: MediaRouter, info: MediaRouter.RouteInfo) {}
        override fun onRouteUngrouped(router: MediaRouter, info: MediaRouter.RouteInfo, group: MediaRouter.RouteGroup) {}
    }

    init {
        mediaRouter.addCallback(MediaRouter.ROUTE_TYPE_LIVE_AUDIO, mediaRouterCallback)
    }
}
