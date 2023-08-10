package com.renad.tabea.ui.inBox

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.core.util.SingleEvent
import com.renad.tabea.domain.model.SortType
import com.renad.tabea.domain.model.Task
import com.renad.tabea.domain.usecase.InBoxScreenUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InBoxViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val useCases: InBoxScreenUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState(tasksStatus = InBoxFragmentArgs.fromSavedStateHandle(savedStateHandle).listStatus))

    val uiState get() = _uiState.asStateFlow()

    private var taskListJop: Job? = null
    private var deleteTaskJob: Job? = null
    private var deleteAllTasksJob: Job? = null
    private var completeTasksJob: Job? = null

    fun onEvent(event: InBoxEvent) {
        when (event) {
            is InBoxEvent.CompleteTask -> completeTask(event.taskId, event.completed)
            is InBoxEvent.DeleteTask -> deleteTask(event.task)
            is InBoxEvent.SortTasks -> loadTaskList(event.sortType)
            InBoxEvent.DeleteTasks -> deleteAllTasks(uiState.value.tasks ?: emptyList())
        }
    }

    private fun completeTask(taskId: Int, completed: Boolean) {
        if (completeTasksJob?.isActive == true) {
            completeTasksJob?.cancel()
        }
        completeTasksJob = useCases.completeTaskUseCase.invoke(taskId, !completed).onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.onEach { _uiState.update { it.copy(isLoading = false) } }
            .flowOn(dispatcher.io).launchIn(viewModelScope)
    }

    fun loadTaskList(sortBy: SortType = SortType.ASC) {
        taskListJop?.cancel()
        taskListJop = loadTasksListFlow(sortBy)
            .flowOn(dispatcher.io)
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun deleteTask(task: Task) {
        if (deleteTaskJob?.isActive == true) {
            deleteTaskJob?.cancel()
        }
        deleteTaskJob = useCases.deleteTaskUseCase.invoke(task).onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.flatMapLatest { loadTasksListFlow() }
            .onEach { _uiState.update { it.copy(isLoading = false) } }
            .flowOn(dispatcher.io).launchIn(viewModelScope)
    }

    private fun loadTasksListFlow(sortBy: SortType = SortType.ASC) =
        useCases.getTasksUseCase.invoke(sortBy, uiState.value.tasksStatus).onEach { task ->
            when (task) {
                Response.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
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
                    _uiState.update {
                        it.copy(
                            tasks = task.data ?: emptyList(),
                            isLoading = false,
                        )
                    }
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun deleteAllTasks(tasks: List<Task>) {
        if (deleteAllTasksJob?.isActive == true) {
            deleteAllTasksJob?.cancel()
        }
        deleteAllTasksJob = useCases.deleteTasksUseCase.invoke(tasks).onStart {
            _uiState.update { it.copy(isLoading = true) }
        }.flatMapLatest { loadTasksListFlow() }
            .onEach { _uiState.update { it.copy(isLoading = false) } }
            .flowOn(dispatcher.io).launchIn(viewModelScope)
    }
}
