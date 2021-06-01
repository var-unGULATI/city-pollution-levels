package com.varunsoft.airquality.data.repository

import android.util.Log
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.varunsoft.airquality.data.model.CityAqiItem
import com.varunsoft.airquality.data.remote.WebSocketClient
import com.varunsoft.airquality.ui.aqlist.AQMFragment
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class AQIDataRepository (var scarletService: WebSocketClient) {

    fun callNetwork():Flowable<List<CityAqiItem>> {

        val subscribe = scarletService.observeWebSocketEvent()
            .filter {
                it is WebSocket.Event.OnConnectionOpened<*>
            }
            .subscribe {
                Log.d(AQMFragment.TAG, "Connection Opened")
            }

        return scarletService.observeAQIData()
    }
}