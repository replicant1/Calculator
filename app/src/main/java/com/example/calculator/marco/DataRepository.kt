package com.example.calculator.marco

class DataRepository {

    fun fetchData(): List<DataModel> {
        return listOf(
            DataModel(1, "Value 1"),
            DataModel(2, "Value 2"),
            DataModel(3, "Value 3"),
            DataModel(4, "Value 4")
        )
    }
}

