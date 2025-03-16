package com.example.calculator

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CalculatorTest {
    // This SUT is *not* mocked
    lateinit var calc : Calculator

    // This SUT is a mock is created by MockK
    @MockK
    lateinit var mockedCalculator: Calculator

    @Before
    fun someSetup() {
        calc = Calculator()
        // Without this, mockedCalculator is not initialised
        // With it, mockedCalculator is re-initialized every time
        MockKAnnotations.init(this)
    }

    @Test
    fun test_mockk_creates_mock_with_no_dependencies() {
        every { mockedCalculator.add(any(), any())} returns 10
        val result = mockedCalculator.add(3, 4)
        assertEquals(10, result)
    }

    @Test
    fun test_add_two_numbers() {
        val result = calc.add(12, 21)
        assertEquals(33, result)
    }

    @Test
    fun test_every_mem() {
        every { mockedCalculator.mem()} returns 5
        val result = mockedCalculator.mem()
        assertEquals(5, result)
    }

    @Test
    fun local_mock_calculator() {
        // Can't use @MockK annotation
        val localMockCalculator = mockk<Calculator>()
        every { localMockCalculator.mem()} returns 5
        val result = localMockCalculator.mem()
        assertEquals(5, result)
    }

    @Test
    fun test_one_wildcard_argument() {
        every { mockedCalculator.add(5, any()) } returns 10
        every { mockedCalculator.add(6, any())} returns 16

        assertEquals(10, mockedCalculator.add(5, 6))
        assertEquals(10, mockedCalculator.add(5, 7))

        assertEquals(16, mockedCalculator.add(6, 1))
        assertEquals(16, mockedCalculator.add(6,8))
    }
}