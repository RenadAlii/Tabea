package com.renad.tabea.ui.upsert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.renad.tabea.core.extensions.collectFlow
import com.renad.tabea.core.extensions.setEditTextValue
import com.renad.tabea.core.extensions.showDatePicker
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
        dateButton.setOnClickListener { viewModel.onEvent(UpsertTaskEvent.ShowDatePicker) }
        doneButton.setOnClickListener { saveTask() }
        addDescription.setOnClickListener {
            showDescriptionLayout(true)
        }
        removeDescription.setOnClickListener {
            showDescriptionLayout(false)
        }
        doneButton.text = viewModel.doneButtonTitle(requireContext())
        taskObserver()
        descriptionObserver()
    }

    private fun FragmentUpsertTaskBinding.showDescriptionLayout(isVisible: Boolean) {
        descriptionLayout.isVisible = isVisible
        addDescription.isVisible = !isVisible
        removeDescription.isVisible = isVisible
    }

    private fun taskObserver() {
        binding.todoEditText.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(UpsertTaskEvent.EditTask(text.toString()))
        }
    }

    private fun descriptionObserver() {
        binding.descriptionLayout.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(UpsertTaskEvent.EditDescription(text.toString()))
        }
    }

    private fun observeState() = collectFlow {
        viewModel.uiState.collect(::renderUiState)
    }

    private fun renderUiState(state: UpsertTaskUiState) = with(state) {
        binding.apply {
            showDatePicker?.handel { showDatePicker(::onHideDatePickerClick) }
            date?.let { dateText.text = it }
            todoEditText.setEditTextValue(task)
            if (description != null) {
                descriptionLayout.setEditTextValue(description)
            }
            errorMsg?.handel { showToast(it) }
            navigateBack?.handel { navigateToInBoxScreen() }
        }
    }

    private fun navigateToInBoxScreen() {
        findNavController().navigate(UpsertTaskFragmentDirections.actionToInBoxFragment())
    }

    private fun onHideDatePickerClick(isCancel: Boolean, date: Long?) {
        viewModel.onEvent(UpsertTaskEvent.HideDatePicker(date, isCancel))
    }

    private fun saveTask() {
        viewModel.onEvent(UpsertTaskEvent.SaveText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
