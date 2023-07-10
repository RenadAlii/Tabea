package com.renad.tabea.ui.edit

import androidx.lifecycle.ViewModel
import com.renad.tabea.data.DataSource
import com.renad.tabea.data.local.TaskDao
import com.renad.tabea.data.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(private val dao: TaskDao) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTaskUiState())
    val uiState get() = _uiState.asStateFlow()

    private var taskId: Int? = null

    fun onEvent(event: EditTaskEvent) {
        when (event) {
            is EditTaskEvent.ShowDatePicker -> {
                _uiState.update {
                    it.copy(showDatePicker = true)
                }
            }

            is EditTaskEvent.HideDatePicker -> {
                _uiState.update {
                    it.copy(
                        showDatePicker = false,
                        date = if (!event.isCancel) event.date else it.date,
                    )
                }
            }

            is EditTaskEvent.HideTimePicker -> {
                _uiState.update {
                    it.copy(
                        showTimePicker = false,
                        time = if (!event.isCancel) event.time else it.time,
                    )
                }
            }

            EditTaskEvent.ShowTimePicker -> {
                _uiState.update {
                    it.copy(showTimePicker = true)
                }
            }

            is EditTaskEvent.EditTask -> {
                _uiState.update {
                    it.copy(task = event.task, description = event.taskDescription)
                }
                editTaskInfo()
            }
        }
    }

    private fun editTaskInfo() = with(uiState.value) {
        if (taskNotEmpty()) {
//            viewModelScope.launch {
//                ao.update(
//                TaskEntity(title = task, details = description, time = time, date = date, id = 11 )
//
//            ) }
        } else { // show error
        }
    }

    private fun mapToTaskEntity() {
//        TaskEntity()
    }

    private fun taskNotEmpty() = uiState.value.task.isNotBlank()

    fun editTask(index: Int, task: Task) = DataSource.editTask(index, task)

    // fun to check if the data is not null
    fun isDataNotEmpty(todoText: String, time: String, date: String): Boolean {
        val todoTaskCheck = isTodoTaskNotEmpty(todoText)
        val timeCheck = isTimeNotEmpty(time)
        val dateCheck = isDateNotEmpty(date)
        return todoTaskCheck && timeCheck && dateCheck
    }

    private fun isDateNotEmpty(date: String): Boolean = date != ""

    private fun isTimeNotEmpty(time: String): Boolean = time != ""

    private fun isTodoTaskNotEmpty(todoText: String): Boolean = todoText != ""
}
