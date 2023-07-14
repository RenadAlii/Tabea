package com.renad.tabea.domain.usecase

import com.renad.tabea.core.util.DateUtil.getDate
import com.renad.tabea.core.util.DateUtil.getTime
import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.domain.TaskRepository
import com.renad.tabea.domain.model.SortType
import com.renad.tabea.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
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
    operator fun invoke(sortBy: SortType = SortType.ASC): Flow<Response<List<Task>>> {
        return taskRepository.getAllTasksSortByASC().onEach { response ->
            when (response) {
                is Response.Success -> {
                    when (sortBy) {
                        SortType.DATE -> response.data?.sortedByDescending { task -> task.date?.getDate() }
                        SortType.TIME -> response.data?.sortedByDescending { task -> task.time?.getTime() }
                        else -> {}
                    }
                }

                else -> {}
            }
        }.flowOn(dispatcher.io)
    }
}
