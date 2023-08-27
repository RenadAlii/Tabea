package com.renad.tabea.core.extensions

import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import com.google.android.material.timepicker.TimeFormat

/**
 *  @return the system time format.
 */
fun Fragment.getSystemHourFormat(): Int {
    val isSystem24Hour = DateFormat.is24HourFormat(requireContext())
    // if the timeFormat in the system is 24 return it 24h else return 12h.
    return if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
}
