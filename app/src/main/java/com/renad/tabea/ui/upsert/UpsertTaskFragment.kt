package com.renad.tabea.ui.upsert

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
import com.renad.tabea.databinding.FragmentUpsertTaskBinding
import kotlinx.coroutines.launch

class UpsertTaskFragment : Fragment() {

    private val viewModel by viewModels<UpsertTaskViewModel>()

    private var _binding: FragmentUpsertTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment.
        _binding = FragmentUpsertTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            // @ because inside binding.apply this revers to the binding instance
            // not the class EditTodoFragment
            editTodoFragment = this@UpsertTaskFragment
            calender.setOnClickListener { viewModel.onEvent(UpsertTaskEvent.ShowDatePicker) }
            time.setOnClickListener { viewModel.onEvent(UpsertTaskEvent.ShowTimePicker) }
            doneButton.text = viewModel.doneButtonTitle(requireContext())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::handelUiState)
            }
        }
    }

    private fun handelUiState(state: UpsertTaskUiState) = with(state) {
        if (showDatePicker) showDatePicker(::onHideDatePickerClick)
        binding.dateText.text = DateUtil.dateFormatter().format(date)
        if (showTimePicker) showTimePicker(::onHideTimePickerClick)
        binding.timeText.text = time
        binding.todoEdittext.editText?.setText(task)
        binding.descriptionEdittext.editText?.setText(description)
        if (errorMsg != null) showToast(errorMsg)
        if (navigateBack) findNavController().navigate(R.id.action_editTodoFragment_to_theListOfTodoFragment)
    }

    private fun onHideDatePickerClick(isCancel: Boolean, date: Long?) {
        viewModel.onEvent(UpsertTaskEvent.HideDatePicker(date, isCancel))
    }

    private fun onHideTimePickerClick(isCancel: Boolean, time: String) {
        viewModel.onEvent(UpsertTaskEvent.HideTimePicker(time, isCancel))
    }

    fun editDataOrShowToast() {
        if (viewModel.isDataNotEmpty(
                binding.todoEdittext.editText?.text.toString(),
                binding.timeText.text.toString(),
                binding.dateText.text.toString(),
            )
        ) {
            // TODO edit task state
            viewModel.onEvent(
                UpsertTaskEvent.EditTask(
                    binding.todoEdittext.editText?.text.toString(),
                    binding.descriptionEdittext.editText?.text.toString(),
                ),
            )
        } else {
            showToast(getString(R.string.empty_task_msg))
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        viewModel.messageShown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
