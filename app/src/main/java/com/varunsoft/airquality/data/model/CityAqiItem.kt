package com.varunsoft.airquality.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class CityAqiItem(

        @field:SerializedName("city")
        val city: String? = null,

        @field:SerializedName("aqi")
        val aqi: String? = null,

        val time: Date? = null
)