package com.renad.tabea.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.renad.tabea.R
import com.renad.tabea.data.model.Todo
import com.renad.tabea.databinding.FragmentEditTodoBinding
import kotlin.properties.Delegates

class EditTodoFragment : Fragment() {
    private val sharedViewModel: TodoViewModel by activityViewModels()
    private var binding: FragmentEditTodoBinding? = null

    lateinit var TODOTASK: String
    lateinit var DATEOFTASK: String
    lateinit var TIMETOFINSH: String
    lateinit var DETAILSOFTASK: String
    lateinit var INDEX: String
    var ISCOMPLETED by Delegates.notNull<Boolean>()

    // to set the key in variable
    companion object {
        const val todoTask = "todoTaskToEdit"
        const val dateOfTask = "dateToEdit"
        const val timeToFinishTask = "timeToEdit"
        const val detailsOfTask = "descriptionToEdit"
        const val indexOfTask = "positionOfTask"
        const val isCompleted = "isCompleted"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment.
        val fragmentBinding = FragmentEditTodoBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            TODOTASK = it.getString(todoTask).toString()
            DETAILSOFTASK = it.getString(detailsOfTask).toString()
            TIMETOFINSH = it.getString(timeToFinishTask).toString()
            DATEOFTASK = it.getString(dateOfTask).toString()
            INDEX = it.getString(indexOfTask).toString()
            ISCOMPLETED = it.getBoolean(isCompleted)
        }
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel

            // @ because inside binding.apply this revers to the binding instance
            // not the class EditTodoFragment
            editTodoFragment = this@EditTodoFragment
            timeText.textDirection
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun editDataOrShowToast() {
        if (sharedViewModel.isDataNotEmpty(
                binding?.todoEdittext?.editText?.text.toString(),
                binding?.timeText?.text.toString(),
                binding?.dateText?.text.toString(),
            )
        ) {
            sharedViewModel.editTask(
                INDEX.toInt(),
                Todo(
                    binding?.todoEdittext?.editText?.text.toString(),
                    binding?.descriptionEdittext?.editText?.text.toString(),
                    sharedViewModel.time.value.toString(),
                    sharedViewModel.date.value.toString(),
                    ISCOMPLETED,
                ),
            )

            findNavController().navigate(R.id.action_editTodoFragment_to_theListOfTodoFragment)
        } else {
            Toast.makeText(context, "please don't let any information empty", Toast.LENGTH_LONG)
                .show()
        }
    }

    // fun to show DatePicker to choice the date deadline of the task.
    fun showDatePicker() {
        // DatePicker, set title of DatePicker,
        // set default selection on today.
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())

        // set Validator to Makes the dates only from today forward selectable.
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        datePicker.setCalendarConstraints(constraintsBuilder.build())

        val picker = datePicker.build()
        picker.show(requireFragmentManager(), picker.toString())
        picker.addOnPositiveButtonClickListener {
            // set the date.
            sharedViewModel.setDate(it)
            binding?.dateText?.text = sharedViewModel.date.value
        }
    }

    // fun to show TimePicker to choice the time deadline of the task.
    fun showTimePicker() {
        // call fun setSystemHourFormat() to return the system time format.
        val clockFormat = sharedViewModel.setSystemHourFormat(requireContext())

        // set timepicker TimeFormat , Hour , Minute & Title then show.
        val timePicker = MaterialTimePicker.Builder()
        timePicker.setTimeFormat(clockFormat).setHour(4).setMinute(30)
            .setTitleText("Select Appointment time").build()
        val picker = timePicker.build()
        picker.show(requireFragmentManager(), "tag")

        // when the user choice the time call setTime() fun.

        picker.addOnPositiveButtonClickListener {
            sharedViewModel.setTime("${picker.hour}:${picker.minute}")
            binding?.timeText?.text = sharedViewModel.time.value
        }
    }
}
