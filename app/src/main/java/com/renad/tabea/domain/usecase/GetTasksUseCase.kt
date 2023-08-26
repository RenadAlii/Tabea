package com.renad.tabea.domain.usecase

import android.util.Log
import com.renad.tabea.core.util.DateUtil.getDate
import com.renad.tabea.core.util.DateUtil.getTime
import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.domain.TaskRepository
import com.renad.tabea.domain.model.SortType
import com.renad.tabea.domain.model.Task
import com.renad.tabea.ui.inBox.TasksStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

/**
 * A use case which obtains a list of tasks.
 */
class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dispatcher: Dispatcher,
) {
    /**
     * Returns a list of tasks.
     *
     * @param sortBy - the field used to sort the tasks. Default NONE = ASC sort.
     */
    operator fun invoke(
        sortBy: SortType = SortType.ASC,
        filterBy: TasksStatus,
    ): Flow<Response<List<Task>>> {
        return taskRepository.getAllTasksSorted(sortBy).onStart {
            emit(Response.Loading)
            Log.e("BASE", "GetTasksUseCase: loading")
        }.transform { response ->
            when (response) {
                is Response.Success -> {
                    emit(Response.Success(sortList(sortBy, filterList(filterBy, response.data))))
                }
                else -> emit(response)
            }
        }.flowOn(dispatcher.io)
    }

    private fun filterList(filterBy: TasksStatus, tasks: List<Task>?) = when (filterBy) {
        TasksStatus.COMPLETED -> tasks?.filter { task -> task.isCompleted }
        TasksStatus.PAST -> tasks?.filter { task -> task.isTaskDayPast }
        TasksStatus.TODAY -> tasks?.filter { task -> task.isTaskDueToday }
        TasksStatus.IN_BOX -> tasks
    }

    private fun sortList(
        sortBy: SortType,
        tasks: List<Task>?,
    ) =
        when (sortBy) {
            SortType.DATE -> tasks?.sortedBy { task -> task.date?.getDate() }
            else -> tasks
        }
}
