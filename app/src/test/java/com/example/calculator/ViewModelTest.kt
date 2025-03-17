package com.example.calculator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ViewModelTest
{
    // To execute LiveData tasks synchronously during testing.
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `test loadUser updates userName LiveData`() {
        val mockUserRepository = mockk<UserRepository>()
        val viewModel = UserViewModel(mockUserRepository)
        val observer = mockk<Observer<String>>(relaxed = true)

        every { mockUserRepository.getUser(1) } returns "John Doe"

        viewModel.userName.observeForever(observer)
        viewModel.loadUser(1)

        verify { observer.onChanged("John Doe")}
    }
}