package com.example.transweather.features.common

import com.example.transweather.features.common.models.Unit


fun convertKelvinToUnit(kelvin: Double?, unit : Unit): Double {
    kelvin ?: return 0.0
    return when (unit) {
        Unit.Celsius -> kelvin - 273.15
        Unit.Fahrenheit -> (kelvin - 273.15) * 1.8 + 32
    }
}