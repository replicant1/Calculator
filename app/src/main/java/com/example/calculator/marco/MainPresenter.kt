package com.example.calculator.marco


class MainPresenter(
    private val view: View, private val dataRepository: DataRepository
) : Presenter {
    override fun fetchData() {
        println("*** Into MainPresenter.fetchData() ****")
        try {
            val result = dataRepository.fetchData()
            view.onResult(result.map { it.toUIDataModel() })
        } catch (ex: Exception) {
            view.onError(ex)
        }
    }
}