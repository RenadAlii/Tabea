package com.renad.tabea.core.extensions

import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.renad.tabea.R

/**
 * displays the DatePicker for selecting the task's date deadline.
 */
fun Fragment.showDatePicker(clickAction: (isCancel: Boolean, date: Long?) -> Unit) {
    // Validator to allow only dates from today forward to be selected.
    val constraintsBuilder = CalendarConstraints.Builder()
        .setValidator(DateValidatorPointForward.now())

    /* 1- set title of DatePicker,
      2- Opens the date picker with today's date selected,
      3- set Validator. */
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(getString(R.string.select_date))
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .setCalendarConstraints(constraintsBuilder.build()).build()
    datePicker.addOnPositiveButtonClickListener { clickAction(false, it) }
    datePicker.addOnCancelListener { clickAction(true, null) }
    datePicker.show(parentFragmentManager, "")
}

/**
 * displays the TimePicker for selecting the task's time deadline.
 */
fun Fragment.showTimePicker(clickAction: (isCancel: Boolean, time: String) -> Unit) {
    val clockFormat = getSystemHourFormat()
    // set timepicker TimeFormat, Input Mode as keyboard & Title.
    val timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(clockFormat)
        .setTitleText(getString(R.string.select_time))
        .setHour(12)
        .setInputMode(INPUT_MODE_KEYBOARD).build()

    timePicker.addOnPositiveButtonClickListener {
        clickAction(false, "${timePicker.hour}:${timePicker.minute}")
    }
    timePicker.addOnNegativeButtonClickListener { clickAction(true, "") }
    timePicker.show(parentFragmentManager, "")
}
