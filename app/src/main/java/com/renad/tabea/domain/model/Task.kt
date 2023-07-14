package com.renad.tabea.domain.model

import com.renad.tabea.core.util.DateUtil
import com.renad.tabea.core.util.DateUtil.getDate
import com.renad.tabea.core.util.DateUtil.getTime

data class Task(
    val id: Int?,
    val task: String?,
    val details: String?,
    val time: String?,
    val date: Long?,
    var isCompleted: Boolean = false,
) {

    val isDateEmpty get() = (date == null)
    val isTaskDueToday get() = date?.getDate()?.compareTo(DateUtil.dateOfToday()) == 0
    val isTaskDayPast get() = date?.getDate()?.before(DateUtil.dateOfToday()) == true
    val isTaskTimePast get() = DateUtil.currentTime()?.after(time?.getTime()) == true
    val taskState: TaskState
        get() = if (!isCompleted) {
            if (isTaskDayPast || isTaskDueToday && isTaskTimePast) {
                TaskState.MISS
            } else {
                TaskState.ACTIVE
            }
        } else {
            TaskState.COMPLETE
        }
}
