package com.varunsoft.airquality.ui.aqlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.varunsoft.airquality.R
import com.varunsoft.airquality.ui.aqgraph.RealtimeLineChartFragment

class MainActivity : AppCompatActivity((R.layout.activity_main)), AQMFragment.ChangeFragment{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            var fragment = supportFragmentManager.findFragmentByTag(AQMFragment.TAG) as AQMFragment?
            if (fragment == null) {
                fragment = AQMFragment.newInstance()
                fragmentTransaction.add(R.id.containerFragment, fragment)
            } else {
                fragmentTransaction.show(fragment)
            }
            fragmentTransaction.commit()
        }

    }

    override fun changeFragment(city:String,aqi:String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(RealtimeLineChartFragment.TAG) as RealtimeLineChartFragment?
        if (fragment == null) {
            fragment = RealtimeLineChartFragment.newInstance()
            var bundle = Bundle()
            bundle.putString("city",city)
            bundle.putString("aqi",aqi)
            fragment!!.arguments = bundle
            fragmentTransaction.add(R.id.containerFragment, fragment)
        }
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}