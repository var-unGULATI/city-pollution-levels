package com.varunsoft.airquality.utils

import android.content.Context
import java.math.BigDecimal
import java.math.RoundingMode

 class Utils {

     companion object {

         const val SECOND_MILLIS = 1000
         const val MINUTE_MILLIS = 60 * SECOND_MILLIS

         public fun round(value: Double, places: Int): Double {
             require(places >= 0)
             var bd = BigDecimal(java.lang.Double.toString(value))
             bd = bd.setScale(places, RoundingMode.HALF_UP)
             return bd.toDouble()
         }

         public fun getTimeAgo(time: Long):String{

             val now = System.currentTimeMillis()

             val diff = (now - time)/1000
             if( diff.toInt() == 0){
                 return "Just Now"
             }
             if(diff < MINUTE_MILLIS){
                 return "$diff seconds ago"
             }
             if(diff < 2 * MINUTE_MILLIS){
                 return "1 minute ago"
             }
             if(diff < 50 * MINUTE_MILLIS){
                 return "minutes ago"
             }
             if(diff < 90 * MINUTE_MILLIS){
                 return "1 hour ago"
             }
             return ""
         }

         fun checkDuration(required:Long, last:Long):Boolean{

             val now = System.currentTimeMillis()

             val diff = (now - last)/1000
             if(diff.toInt() > required){
                 return true
             }
             return false
         }
     }
}