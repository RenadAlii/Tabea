package com.example.tabea.data

import com.example.tabea.model.Todo

object DataSource {
    var dataSet: MutableList<Todo> = mutableListOf(
        Todo("1", "go to the school","at time", "12:30", "20/5")
        , Todo("2", "program my app","3 fragment", "12:30", "30/4")
        , Todo("3", "sleep","at 12", "02:11", "01/03")
    )
}