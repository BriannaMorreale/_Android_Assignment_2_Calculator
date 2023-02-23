package com.example.assignment_2_calculator.data

import com.example.assignment_2_calculator.R
import com.example.assignment_2_calculator.model.appName

class Datasource {
    fun loadAppName(): List<appName>{
        return listOf<appName>(
            appName(R.string.name_of_app),
            appName(R.string.desc)
                )
    }
}