package com.renad.tabea.ui.upsert

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renad.tabea.R
import com.renad.tabea.core.util.DateUtil
import com.renad.tabea.core.util.DateUtil.getTime
import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.core.util.WhileUiSubscribed
import com.renad.tabea.domain.TaskRepository
import com.renad.tabea.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
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
    @ActivityContext val context: Context,
) : ViewModel() {

    private val taskId: Int? = UpsertTaskFragmentArgs.fromSavedStateHandle(savedStateHandle).taskId

    private val _uiState = MutableStateFlow(UpsertTaskUiState())

    val uiState: StateFlow<UpsertTaskUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = UpsertTaskUiState(isLoading = true),
    )

    init {
        if (taskId != null) setTask()
    }

    fun doneButtonTitle(context: Context): String =
        if (taskId == null) context.getString(R.string.add) else context.getString(R.string.edit)

    private fun setTask() {
        taskRepository.getTaskById(taskId!!).map { task ->
            when (task) {
                Response.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is Response.Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMsg = task.message,
                            isLoading = false,
                        )
                    }
                }

                is Response.Success -> {
                    val taskDetails = task.data
                    _uiState.update {
                        it.copy(
                            task = taskDetails?.task ?: "",
                            time = taskDetails?.time,
                            date = DateUtil.dateFormatter().format(taskDetails?.date),
                            isLoading = false,
                            isCompleted = taskDetails?.isCompleted ?: false,
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
                    it.copy(showDatePicker = true)
                }
            }

            is UpsertTaskEvent.HideDatePicker -> {
                _uiState.update {
                    it.copy(
                        showDatePicker = false,
                        date = if (!event.isCancel) event.date.toString() else it.date,
                    )
                }
            }

            is UpsertTaskEvent.HideTimePicker -> {
                _uiState.update {
                    it.copy(
                        showTimePicker = false,
                        time = if (!event.isCancel) event.time else it.time,
                    )
                }
            }

            UpsertTaskEvent.ShowTimePicker -> {
                _uiState.update {
                    it.copy(showTimePicker = true)
                }
            }

            is UpsertTaskEvent.EditTask -> {
                _uiState.update {
                    it.copy(task = event.task, description = event.taskDescription)
                }
                editTaskInfo()
            }
        }
    }

    private fun editTaskInfo() = with(uiState.value) {
        taskRepository.upsert(
            Task(
                id = taskId,
                task = task,
                details = description,
                time = time,
                date = date?.getTime()?.time,
            ),
        ).onStart { _uiState.update { it.copy(isLoading = true) } }.map { response ->
            when (response) {
                is Response.Failure -> _uiState.update {
                    it.copy(
                        errorMsg = response.message,
                        isLoading = false,
                    )
                }

                is Response.Success -> _uiState.update {
                    it.copy(
                        errorMsg = context.getString(R.string.task_added_successfully),
                        isLoading = false,
                        navigateBack = true,
                    )
                }

                else -> {}
            }
        }.flowOn(dispatcher.io).launchIn(viewModelScope)
    }

    // fun to check if the data is not null
    fun isDataNotEmpty(todoText: String, time: String, date: String): Boolean =
        isTodoTaskNotEmpty(todoText) && isTimeNotEmpty(time) && isDateNotEmpty(date)

    private fun isDateNotEmpty(date: String): Boolean = date != ""

    private fun isTimeNotEmpty(time: String): Boolean = time != ""

    private fun isTodoTaskNotEmpty(todoText: String): Boolean = todoText != ""

    fun messageShown() {
        _uiState.update { it.copy(errorMsg = null) }
    }
}
