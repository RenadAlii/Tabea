package com.renad.tabea.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.renad.tabea.R
import com.renad.tabea.core.extensions.showDatePicker
import com.renad.tabea.core.extensions.showTimePicker
import com.renad.tabea.core.util.DateUtil
import com.renad.tabea.databinding.FragmentEditTaskBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class EditTaskFragment : Fragment() {

    private val editTaskViewModel by viewModels<EditTaskViewModel>()

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    lateinit var TODOTASK: String
    lateinit var DATEOFTASK: String
    lateinit var TIMETOFINSH: String
    lateinit var DETAILSOFTASK: String
    lateinit var INDEX: String
    var ISCOMPLETED by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            TODOTASK = it.getString(todoTask).toString()
            DETAILSOFTASK = it.getString(detailsOfTask).toString()
            TIMETOFINSH = it.getString(timeToFinishTask).toString()
            DATEOFTASK = it.getString(dateOfTask).toString()
            INDEX = it.getString(indexOfTask).toString()
            ISCOMPLETED = it.getBoolean(isCompleted)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment.
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = editTaskViewModel

            // @ because inside binding.apply this revers to the binding instance
            // not the class EditTodoFragment
            editTodoFragment = this@EditTaskFragment
            calender.setOnClickListener { editTaskViewModel.onEvent(EditTaskEvent.ShowDatePicker) }
            time.setOnClickListener { editTaskViewModel.onEvent(EditTaskEvent.ShowTimePicker) }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editTaskViewModel.uiState.collect(::handelUiState)
            }
        }
    }

    private fun handelUiState(state: EditTaskUiState) = with(state) {
        if (showDatePicker) showDatePicker(::onHideDatePickerClick)
        binding.dateText.text = DateUtil.dateFormatter().format(date)
        if (showTimePicker) showTimePicker(::onHideTimePickerClick)
        binding.timeText.text = time
    }
    private fun onHideDatePickerClick(isCancel: Boolean, date: Long?) {
        editTaskViewModel.onEvent(EditTaskEvent.HideDatePicker(date, isCancel))
    }
    private fun onHideTimePickerClick(isCancel: Boolean, time: String) {
        editTaskViewModel.onEvent(EditTaskEvent.HideTimePicker(time, isCancel))
    }
    fun editDataOrShowToast() {
        if (editTaskViewModel.isDataNotEmpty(
                binding.todoEdittext.editText?.text.toString(),
                binding.timeText.text.toString(),
                binding.dateText.text.toString(),
            )
        ) {
            // TODO edit task state
            editTaskViewModel.onEvent(EditTaskEvent.EditTask(binding.todoEdittext.editText?.text.toString(), binding.descriptionEdittext.editText?.text.toString()))

            findNavController().navigate(R.id.action_editTodoFragment_to_theListOfTodoFragment)
        } else {
            Toast.makeText(context, "please don't let any information empty", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // to set the key in variable
    companion object {
        const val todoTask = "todoTaskToEdit"
        const val dateOfTask = "dateToEdit"
        const val timeToFinishTask = "timeToEdit"
        const val detailsOfTask = "descriptionToEdit"
        const val indexOfTask = "positionOfTask"
        const val isCompleted = "isCompleted"
    }
}
