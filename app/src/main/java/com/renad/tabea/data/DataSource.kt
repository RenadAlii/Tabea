package com.renad.tabea.data

import com.renad.tabea.domain.model.Task

object DataSource {
    var taskList: MutableList<Task> = mutableListOf(
//        Task("STUDY FOR MY EXAM", "FROM KOTLIN WEBSITE", "4:30", "21/11/2021", false),
//        Task("FINSH my TOdO list app", "", "12:30", "20/11/2021", true),
//        Task("watch my MARVEL MOVIE", "at 8", "02:11", "18/11/2021", false),

    )

    fun setIsCompleted(completedState: Boolean, task: Task) {
        taskList.find { task == it }?.isCompleted = completedState
    }

    fun getTasksSize(): Int = taskList.size

    fun addNewTask(task: Task) {
        taskList.add(task)
    }

    fun deleteTask(task: Task) = taskList.remove(task)

    fun editTask(position: Int, newTask: Task) {
        taskList[position] = newTask
        println("here= " + taskList[position])
    }

    fun sortByDate() {
//        taskList.sortWith(
//            compareBy<Task> {
//                it.date.slice(0..1).toInt()
//            }.thenBy { it.date.slice(3..4).toInt() }
//                .thenBy { it.date.slice(6 until it.date.length).toInt() },
//        )
        println(taskList)
    }

    fun clearList() = taskList.clear()
}
