package com.renad.tabea.data.model

data class Task(
    val todoText: String,
    val details: String,
    val time: String,
    val date: String,
    var isCompleted: Boolean,
    val taskState: TaskState,
)
