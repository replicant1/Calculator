package com.example.calculator.marco

data class UiDataModel(val uuid: String, val id: Int, val name: String) {
    fun toDataModel() : DataModel {
        return DataModel(id, name)
    }
}