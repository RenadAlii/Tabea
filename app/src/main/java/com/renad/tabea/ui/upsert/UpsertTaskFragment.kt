package com.renad.tabea.ui.upsert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.renad.tabea.R
import com.renad.tabea.core.extensions.collectFlow
import com.renad.tabea.core.extensions.showDatePicker
import com.renad.tabea.core.extensions.showTimePicker
import com.renad.tabea.core.extensions.showToast
import com.renad.tabea.databinding.FragmentUpsertTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpsertTaskFragment : Fragment() {

    private val viewModel by viewModels<UpsertTaskViewModel>()

    private var _binding: FragmentUpsertTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpsertTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeState()
    }

    private fun initViews() = binding.apply {
        calender.setOnClickListener { viewModel.onEvent(UpsertTaskEvent.ShowDatePicker) }
        time.setOnClickListener { viewModel.onEvent(UpsertTaskEvent.ShowTimePicker) }
        doneButton.setOnClickListener { validateTask() }
        doneButton.text = viewModel.doneButtonTitle(requireContext())
    }
    private fun observeState() = collectFlow {
        viewModel.uiState.collect(::renderUiState)
    }

    private fun renderUiState(state: UpsertTaskUiState) = with(state) {
        showDatePicker?.handel { showDatePicker(::onHideDatePickerClick) }
        date?.let { binding.dateText.text = it }
        showTimePicker?.handel { showTimePicker(::onHideTimePickerClick) }
        binding.timeText.text = time
        binding.todoEdittext.editText?.setText(task)
        binding.descriptionEdittext.editText?.setText(description)
        errorMsg?.handel { showToast(it) }
        navigateBack?.handel { navigateToInBoxScreen() }
    }

    private fun navigateToInBoxScreen() {
        findNavController().navigate(UpsertTaskFragmentDirections.actionToInBoxFragment())
    }

    private fun onHideDatePickerClick(isCancel: Boolean, date: Long?) {
        viewModel.onEvent(UpsertTaskEvent.HideDatePicker(date, isCancel))
    }

    private fun onHideTimePickerClick(isCancel: Boolean, time: String) {
        viewModel.onEvent(UpsertTaskEvent.HideTimePicker(time, isCancel))
    }

    private fun validateTask() {
        if (viewModel.isValidTask(binding.todoEdittext.editText?.text.toString())) {
            upsertTask()
        } else {
            showToast(R.string.empty_task_msg)
        }
    }

    private fun upsertTask() {
        viewModel.onEvent(
            UpsertTaskEvent.EditTask(
                binding.todoEdittext.editText?.text.toString(),
                binding.descriptionEdittext.editText?.text.toString(),
            ),
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
