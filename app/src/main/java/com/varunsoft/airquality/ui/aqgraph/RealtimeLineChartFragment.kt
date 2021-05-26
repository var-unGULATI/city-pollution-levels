package com.varunsoft.airquality.ui.aqgraph

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.varunsoft.airquality.R
import com.varunsoft.airquality.ui.aqlist.AQMViewModel

class RealtimeLineChartFragment : Fragment() {

    private val viewModel: AQMViewModel by activityViewModels()

    lateinit var chart: LineChart
    protected var tfRegular: Typeface? = null
    protected var tfLight: Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tfRegular = Typeface.createFromAsset(requireActivity().assets, "OpenSans-Regular.ttf")
        tfLight = Typeface.createFromAsset(requireActivity().assets, "OpenSans-Light.ttf")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.activity_realtime_linechart,container,false)

        chart = view.findViewById(R.id.chart1)
        // enable description text
        chart.getDescription().isEnabled = true

        var city = requireArguments().getString("city")
        var aqi = requireArguments().getString("aqi")!!.toDouble()

        requireActivity().title = city + " Realtime AQI"

        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true)

        // set an alternative background color
        chart.setBackgroundColor(Color.BLACK)
        val data = LineData()
        data.setValueTextColor(Color.WHITE)

        // add empty data
        chart.setData(data)

        // get the legend (only possible after setting data)
        val l = chart.getLegend()

        // modify the legend ...
        l.form = Legend.LegendForm.LINE
        l.typeface = tfLight
        l.textColor = Color.WHITE
        val xl = chart.getXAxis()
        xl.typeface = tfLight
        xl.textColor = Color.WHITE
        xl.setDrawGridLines(true)
        xl.setAvoidFirstLastClipping(true)
        xl.isEnabled = false
        val leftAxis = chart.getAxisLeft()
        leftAxis.typeface = tfLight
        leftAxis.textColor = Color.WHITE
        if(aqi - 100 > 0){
            leftAxis.axisMinimum = (aqi - 50).toFloat()
        }
        else{
            leftAxis.axisMinimum = 0f
        }
        leftAxis.axisMaximum = (aqi + 50).toFloat()
        leftAxis.setDrawGridLines(true)
        val rightAxis = chart.getAxisRight()
        rightAxis.isEnabled = true

        addEntry(requireArguments().getString("aqi")!!.toDouble())
        viewModel.aqiLiveData.observe(viewLifecycleOwner, Observer {
            addEntry(it)
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().title = "Current AQI levels"
        viewModel.cityName = ""
        viewModel.aqiLiveData = MutableLiveData<Double>()
    }

    private fun addEntry(double: Double) {
        val data = chart!!.data
        if (data != null) {
            var set = data.getDataSetByIndex(0)
            // set.addEntry(...); // can be called as well
            if (set == null) {
                set = createSet()
                data.addDataSet(set)
            }
            data.addEntry(
                Entry(
                    set.entryCount.toFloat(), double.toFloat()
                ), 0
            )
            data.notifyDataChanged()

            // let the chart know it's data has changed
            chart!!.notifyDataSetChanged()

            // limit the number of visible entries
            chart!!.setVisibleXRangeMaximum(120f)
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            chart!!.moveViewToX(data.entryCount.toFloat())
        }
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "Dynamic Data")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.color = ColorTemplate.getHoloBlue()
        set.setCircleColor(Color.WHITE)
        set.lineWidth = 2f
        set.circleRadius = 4f
        set.fillAlpha = 65
        set.fillColor = ColorTemplate.getHoloBlue()
        set.highLightColor = Color.rgb(244, 117, 117)
        set.valueTextColor = Color.WHITE
        set.valueTextSize = 9f
        set.setDrawValues(true)
        return set
    }

    companion object{
        fun newInstance() = RealtimeLineChartFragment()
        const val TAG = "GraphFragment"
    }

}