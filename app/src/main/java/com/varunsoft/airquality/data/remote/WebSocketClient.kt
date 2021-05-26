package com.varunsoft.airquality.data.remote

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.varunsoft.airquality.data.model.CityAqiItem
import io.reactivex.Flowable

interface WebSocketClient {

    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>

    @Receive
    fun observeAQIData(): Flowable<List<CityAqiItem>>
}