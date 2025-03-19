package com.example.calculator.marco

import java.util.UUID

data class DataModel(val id: Int, val name : String) {
    fun toUIDataModel(): UiDataModel {
        return UiDataModel(
            MyUtilObject.generateUUID(),
            id,
            name)
    }
}