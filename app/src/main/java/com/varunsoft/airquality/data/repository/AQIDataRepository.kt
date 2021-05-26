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

class AQIDataRepository {

    fun callNetwork():Flowable<List<CityAqiItem>> {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        val scarletInstance = Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory("wss://city-ws.herokuapp.com/"))
            .addMessageAdapterFactory(GsonMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()

        val scarletService = scarletInstance.create<WebSocketClient>()
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