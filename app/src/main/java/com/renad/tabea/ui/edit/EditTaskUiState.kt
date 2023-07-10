package com.renad.tabea.ui.edit

data class EditTaskUiState(
    val task: String = "",
    val description: String = "",
    val date: Long? = null,
    val time: String = "",
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val clickEdit: Boolean = false,
    val navigateBack: Boolean = false,
    val errorMsg: Boolean = true
)
