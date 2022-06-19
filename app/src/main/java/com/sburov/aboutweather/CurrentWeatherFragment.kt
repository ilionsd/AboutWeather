package com.sburov.aboutweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment

class CurrentWeatherFragment : Fragment() {
    private lateinit var currentWeatherImageView: AppCompatImageView
    private lateinit var currentTemperatureTextView: AppCompatTextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        currentWeatherImageView = view.findViewById(R.id.currentWeatherImageView)!!
        currentTemperatureTextView = view.findViewById(R.id.currentTemperatureTextView)!!
        return view
    }
}