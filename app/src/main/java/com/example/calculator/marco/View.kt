package com.example.calculator.marco

interface View {
    fun onResult(result: List<UiDataModel>)
    fun onError(error: Throwable)
}