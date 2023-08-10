package com.renad.tabea.ui.inBox

import androidx.annotation.Keep
import com.renad.tabea.core.util.SingleEvent
import com.renad.tabea.domain.model.SortType
import com.renad.tabea.domain.model.Task

/**
 * edit or delete task
 * delete all tasks
 * sort tasks 1,2,3
 * complete task
 * add task
 */
sealed class InBoxEvent {
    data class CompleteTask(val taskId: Int, val completed: Boolean) : InBoxEvent()
    data class DeleteTask(val task: Task) : InBoxEvent()
    object DeleteTasks : InBoxEvent()
    data class SortTasks(val sortType: SortType) : InBoxEvent()
}

data class TasksUiState(
    val tasks: List<Task>? = null,
    val showEmptyTaskView: Boolean = false,
    val errorMsg: SingleEvent<Int>? = null,
    val isLoading: Boolean = true,
    val tasksStatus: TasksStatus = TasksStatus.IN_BOX,
)

@Keep
enum class TasksStatus {
    COMPLETED,
    PAST,
    TODAY,
    IN_BOX,
}
