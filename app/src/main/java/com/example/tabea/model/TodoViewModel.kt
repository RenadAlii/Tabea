package com.example.tabea.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodoViewModel: ViewModel() {

    private var _id = MutableLiveData<String>()
    val id get() = _id


    private var _toDo = MutableLiveData<String>()
    val toDo get() = _toDo

    private var _isChecked = MutableLiveData<Boolean>(false)
    val isChecked get() = _isChecked

    private var _description = MutableLiveData<String>()
    val description get() = _description

    private var _time = MutableLiveData<String>()
    val time get() = _time

    private var _date = MutableLiveData<String>()
    val date get() = _date




}