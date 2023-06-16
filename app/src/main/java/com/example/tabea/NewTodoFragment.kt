package com.example.tabea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tabea.databinding.FragmentNewTodoBinding
import com.example.tabea.model.TodoViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker

class NewTodoFragment : Fragment() {

    private val sharedViewModel: TodoViewModel by activityViewModels()
    private var binding: FragmentNewTodoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the xml for this fragment.
        val fragmentBinding = FragmentNewTodoBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            // @ because inside binding.apply this revers to the binding instance
            // not the class NewTodoFragment
            newTodoFragment = this@NewTodoFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun goToAddDataOrShowToast() {
        if (sharedViewModel.isDataNotEmpty(
                binding?.todoEdittext?.editText?.text.toString(),
                sharedViewModel.time.value.toString(),
                sharedViewModel.date.value.toString(),
            )
        ) {
            sharedViewModel.addTodoTask(
                binding?.todoEdittext?.editText?.text.toString(),
                binding?.descriptionEdittext?.editText?.text.toString(),
            )
            findNavController().navigate(R.id.action_newTodoFragment_to_theListOfTodoFragment)
        } else {
            Toast.makeText(context, "please enter all the information", Toast.LENGTH_LONG).show()
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
