package com.varunsoft.airquality.ui.aqlist

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.varunsoft.airquality.data.model.CityAqiItem
import com.varunsoft.airquality.data.repository.AQIDataRepository
import com.varunsoft.airquality.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class AQMViewModel @Inject constructor(val aqiDataRepository: AQIDataRepository) : ViewModel() {

    var aqiLiveData = MutableLiveData<Double>()
    var aqiDataList = MutableLiveData<MutableList<CityAqiItem>>()
    var loading: MutableLiveData<Boolean> = MutableLiveData(true)

    var cityName: String = ""
    var last: Long = System.currentTimeMillis()
    var hashMap: MutableMap<String,ArrayList<Pair<Double,Date>>> = LinkedHashMap()


    @SuppressLint("CheckResult")
    fun callNetwork(){
        aqiDataRepository.callNetwork()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                loading.value = false
                it.forEach {
                    if(hashMap.containsKey(it.city)){
                        hashMap.get(it.city)!!.add(Utils.round(it.aqi!!.toDouble(),2) to Calendar.getInstance().getTime())
                    }
                    else{
                        val list = ArrayList<Pair<Double, Date>>()
                        list.add(Utils.round(it.aqi!!.toDouble(),2) to Calendar.getInstance().getTime())
                        hashMap.put(it.city!!,list)
                    }
                }
                val cityList: MutableList<CityAqiItem> = ArrayList()
                hashMap.forEach { (key, value) ->
                    cityList.add(CityAqiItem(key,value.last().component1().toString(),value.last().component2()))
                }
                aqiDataList.postValue(cityList)

                if(Utils.checkDuration(GRAPH_DURATION_SEC, last) && cityName.isNotBlank()) {
                    last = System.currentTimeMillis()
                    aqiLiveData.postValue(hashMap.get(cityName)!!.last().component1())
                }
            }
    }

    companion object{
        const val GRAPH_DURATION_SEC = 15L
    }
}