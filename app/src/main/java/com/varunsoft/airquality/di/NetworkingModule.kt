package com.varunsoft.airquality.di

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.varunsoft.airquality.data.remote.WebSocketClient
import com.varunsoft.airquality.data.repository.AQIDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkingModule  {

    @Provides
    @Singleton
    fun provideScarletInstance(okHttpClient:OkHttpClient):WebSocketClient{

        val scarletInstance = Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory("wss://city-ws.herokuapp.com/"))
            .addMessageAdapterFactory(GsonMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()

        return scarletInstance.create<WebSocketClient>()
    }

    @Provides
    @Singleton
    fun providesHttpClient():OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    fun providesRepository(webSocketClient: WebSocketClient):AQIDataRepository{
        return AQIDataRepository(webSocketClient)
    }
}