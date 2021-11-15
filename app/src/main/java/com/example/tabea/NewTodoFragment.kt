package com.example.tabea

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainer
import com.example.tabea.adapter.TodoListAdapter
import com.example.tabea.databinding.FragmentNewTodoBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*


class NewTodoFragment : Fragment() {

   private var _binding: FragmentNewTodoBinding? = null
    private val binding get() =  _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         _binding = FragmentNewTodoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
///here hold the image and put the date picker
        binding.calender.setOnClickListener {
            showDatePicker()
        }
        binding.time.setOnClickListener {
            showTimePicker()
        }

}



   private fun showDatePicker(){
       val sdf = SimpleDateFormat("dd/MM/yy")


       //Date Picker
        val datePicker = MaterialDatePicker.Builder.datePicker()
        //set the title of the datePicker
        datePicker.setTitleText("Select Date")
        //set the default selection on today
        datePicker.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        // set Validator to Makes the dates only from today forward selectable.
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
     datePicker.setCalendarConstraints(constraintsBuilder.build())

        val picker = datePicker.build()
        picker.show(requireFragmentManager(), picker.toString())

        //set the date on variable
        val date = picker.selection

       val date2 =sdf.format(date)

       binding.dateText.text = date2
    }


    private fun showTimePicker(){

        //variable to make the time format match the device time format
        val isSystem24Hour = is24HourFormat(context)
        //if the timeFormat in the system is 24 makes it 24h else makes it 12h
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

       //set timepicker TimeFormat , Hour , Minute & Title then show
        val timePicker = MaterialTimePicker.Builder()
        timePicker.setTimeFormat(clockFormat).setHour(4).setMinute(30)
            .setTitleText("Select Appointment time").build()
        val picker = timePicker.build()
           picker.show(requireFragmentManager(),"tag")

        picker.addOnPositiveButtonClickListener {
            binding.timeText.text = picker.hour.toString() + picker.minute.toString()
                }



    }








    }