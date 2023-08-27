package com.renad.tabea.ui.upsert

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renad.tabea.R
import com.renad.tabea.core.util.DateUtil
import com.renad.tabea.core.util.DateUtil.getDate
import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.core.util.SingleEvent
import com.renad.tabea.core.util.WhileUiSubscribed
import com.renad.tabea.domain.TaskRepository
import com.renad.tabea.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UpsertTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dispatcher: Dispatcher,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val taskId: Int? =
        UpsertTaskFragmentArgs.fromSavedStateHandle(savedStateHandle).taskId?.toIntOrNull()

    private val _uiState = MutableStateFlow(UpsertTaskUiState())

    val uiState: StateFlow<UpsertTaskUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = UpsertTaskUiState(isLoading = true),
    )

    private var _date: MutableStateFlow<Long?> = MutableStateFlow(null)
    val date get() = _date

    init {
        if (taskId != null) setTask()
    }

    fun doneButtonTitle(context: Context): String =
        if (taskId == null) context.getString(R.string.add) else context.getString(R.string.edit)

    private fun setTask() {
        taskRepository.getTaskById(taskId!!).map { task ->
            when (task) {
                Response.Loading -> {
                    showLoading()
                }

                is Response.Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMsg = SingleEvent(task.messageId),
                            isLoading = false,
                        )
                    }
                }

                is Response.Success -> {
                    val taskDetails = task.data
                    val taskDate = taskDetails?.date
                    _date.update { taskDate }
                    _uiState.update {
                        it.copy(
                            task = taskDetails?.task ?: "",
                            date = taskDate?.getDate()
                                ?.let { stringDate -> DateUtil.dateFormatter().format(stringDate) },
                            isLoading = false,
                            isCompleted = taskDetails?.isCompleted ?: false,
                            description = taskDetails?.details ?: "",
                        )
                    }
                }
            }
        }.flowOn(dispatcher.io).launchIn(viewModelScope)
    }

    fun onEvent(event: UpsertTaskEvent) {
        when (event) {
            is UpsertTaskEvent.ShowDatePicker -> {
                _uiState.update {
                    it.copy(showDatePicker = SingleEvent(Unit))
                }
            }

            is UpsertTaskEvent.HideDatePicker -> {
                if (!event.isCancel) {
                    _date.update { event.date }
                }
                _uiState.update {
                    it.copy(
                        date = if (!event.isCancel) {
                            DateUtil.dateFormatter()
                                .format(event.date?.getDate()!!)
                        } else {
                            it.date
                        },
                        showDatePicker = null,
                    )
                }
            }

            is UpsertTaskEvent.EditTask -> {
                _uiState.update {
                    it.copy(task = event.task)
                }
            }

            is UpsertTaskEvent.EditDescription -> _uiState.update {
                it.copy(description = event.taskDescription)
            }

            UpsertTaskEvent.SaveText -> {
                showLoading()
                validateTaskInfo()
            }
        }
    }

    private fun showLoading() {
        _uiState.update {
            it.copy(
                isLoading = true,
            )
        }
    }

    private fun validateTaskInfo() {
        if (uiState.value.task.isNotBlank()) {
            saveTaskInfo()
        } else {
            _uiState.update {
                it.copy(
                    errorMsg = SingleEvent(R.string.empty_task_msg),
                    isLoading = false,
                )
            }
        }
    }

    private fun saveTaskInfo() = with(uiState.value) {
        taskRepository.upsert(
            Task(
                id = taskId,
                task = task,
                details = description,
                date = _date.value,
            ),
        ).onStart { showLoading() }.map { response ->
            when (response) {
                is Response.Failure -> _uiState.update {
                    it.copy(
                        errorMsg = SingleEvent(response.messageId),
                        isLoading = false,
                    )
                }

                is Response.Success -> _uiState.update {
                    it.copy(
                        errorMsg = SingleEvent(R.string.task_added_successfully),
                        isLoading = false,
                        navigateBack = SingleEvent(Unit),
                    )
                }

                else -> {}
            }
        }.flowOn(dispatcher.io).launchIn(viewModelScope)
    }
}
