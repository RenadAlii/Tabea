package com.renad.tabea.ui.upsert

data class UpsertTaskUiState(
    val task: String = "",
    val description: String? = "",
    val date: String? = null,
    val time: String? = "",
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val clickEdit: Boolean = false,
    val navigateBack: Boolean = false,
    val errorMsg: String? = "",
    val isLoading: Boolean = true,
    val isCompleted: Boolean = false,
)
