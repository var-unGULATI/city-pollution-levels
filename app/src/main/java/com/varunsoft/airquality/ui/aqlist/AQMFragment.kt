package com.varunsoft.airquality.ui.aqlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.varunsoft.airquality.R
import com.varunsoft.airquality.databinding.AQMFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AQMFragment : Fragment() {

    lateinit var binding: AQMFragmentBinding
    lateinit var postsAdapter: AqiCityListAdapter

    private val viewModel: AQMViewModel by activityViewModels()

    companion object {
        fun newInstance() = AQMFragment()
        const val TAG = "AQM Fragment"
    }

    lateinit var changeFragment: ChangeFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postsAdapter = AqiCityListAdapter(context){ city, aqi ->
            viewModel.cityName = city
            changeFragment.changeFragment(city,aqi)
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.a_q_m_fragment, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        changeFragment = requireActivity() as ChangeFragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.callNetwork()
        viewModel.aqiDataList.observe(requireActivity(), {
            postsAdapter.addData(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = postsAdapter
        }

    }

    interface ChangeFragment{
        fun changeFragment(city:String,aqi:String)
    }
}

