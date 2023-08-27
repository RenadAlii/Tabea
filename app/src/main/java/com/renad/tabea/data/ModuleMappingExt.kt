package com.renad.tabea.data

import com.renad.tabea.data.local.LocalTask
import com.renad.tabea.domain.model.Task

/**
 * Data model mapping extension functions. There are 2 model types:
 *
 * - Task: External model exposed to other layers in the architecture.
 * Obtained using `toExternal`.
 *
 * - LocalTask: Internal model used to represent a task stored locally in a database. Obtained
 * using `toLocal`.
 *
 */

// External to local
fun Task.toLocal() =
    LocalTask(
        id = id,
        task = task,
        details = details,
        isCompleted = isCompleted,
        date = date,
    )

fun List<Task>.toLocal() = map(Task::toLocal)

// Local to External
fun LocalTask.toExternal() = Task(
    id = id,
    task = task,
    details = details,
    isCompleted = isCompleted,
    date = date,
)

fun List<LocalTask>.toExternal() = map(LocalTask::toExternal)
