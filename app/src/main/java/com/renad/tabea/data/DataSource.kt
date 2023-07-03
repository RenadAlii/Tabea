package com.renad.tabea.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.renad.tabea.data.model.Todo

object DataSource {
    var taskList: MutableList<Todo> = mutableListOf(
        Todo("STUDY FOR MY EXAM", "FROM KOTLIN WEBSITE", "4:30", "21/11/2021", false),
        Todo("FINSH my TOdO list app", "", "12:30", "20/11/2021", true),
        Todo("watch my MARVEL MOVIE", "at 8", "02:11", "18/11/2021", false),

    )

    fun setIsCompleted(completedState: Boolean, task: Todo) {
        taskList.find { task == it }?.isCompleted = completedState
    }
    fun getTasksSize(): Int = taskList.size

    fun addNewTask(task: Todo) {
        taskList.add(task)
    }

    fun deleteTask(task: Todo) = taskList.remove(task)

    fun editTask(position: Int, newTask: Todo) {
        taskList.set(position, newTask)
        println("here= " + taskList[position])
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sortByDES() = taskList.sortWith(compareBy<Todo> { it.todoText[0].uppercase() }.reversed())
    fun sortByDate() {
        taskList.sortWith(
            compareBy<Todo> {
                it.date.slice(0..1).toInt()
            }.thenBy { it.date.slice(3..4).toInt() }
                .thenBy { it.date.slice(6 until it.date.length).toInt() },
        )
        println(taskList)
    }
    fun sortByAES() = taskList.sortWith(compareBy<Todo> { it.todoText[0].uppercase() })

    fun clearList() = taskList.clear()
}
