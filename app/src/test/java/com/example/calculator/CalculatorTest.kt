package com.example.calculator

import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CalculatorTest {
    // This SUT is *not* mocked
    lateinit var calc: Calculator

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
        every { mockedCalculator.add(any(), any()) } returns 10
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
        every { mockedCalculator.mem() } returns 5
        val result = mockedCalculator.mem()
        assertEquals(5, result)
    }

    @Test
    fun local_mock_calculator() {
        // Can't use @MockK annotation
        val localMockCalculator = mockk<Calculator>()
        every { localMockCalculator.mem() } returns 5
        val result = localMockCalculator.mem()
        assertEquals(5, result)
    }

    @Test
    fun test_one_wildcard_argument() {
        every { mockedCalculator.add(5, any()) } returns 10
        every { mockedCalculator.add(6, any()) } returns 16

        assertEquals(10, mockedCalculator.add(5, 6))
        assertEquals(10, mockedCalculator.add(5, 7))

        assertEquals(16, mockedCalculator.add(6, 1))
        assertEquals(16, mockedCalculator.add(6, 8))
    }

    @Test
    fun test_verify_direct_call() {
        val thingOne = mockk<ThingOne>()
        every { thingOne.doSomethingOne() } answers { }
        thingOne.doSomethingOne()
        verify { thingOne.doSomethingOne() }
    }

    @Test
    fun test_verify_just_run() {
        val thingOne = mockk<ThingOne>()
        val thingTwo = ThingTwo()
        justRun { thingOne.doSomethingOne() } // Alt to "answers {}" when there is no return value
        thingTwo.doSomethingOneTwice(thingOne)
        verify { thingOne.doSomethingOne() }
    }

    @Test
    fun test_verify_indirect_call() {
        val thingOne = mockk<ThingOne>()
        val thingTwo = ThingTwo()
        every { thingOne.doSomethingOne() } answers {}
        thingTwo.doSomethingOneTwice(thingOne)
        verify { thingOne.doSomethingOne() }
    }

    @Test
    fun test_spy_avoids_no_answer_error() {
        val thingOne = spyk<ThingOne>()
        val thingTwo = spyk<ThingTwo>()
        thingTwo.doSomethingOneTwice(thingOne)
        verify { thingOne.doSomethingOne() }
    }

    @Test
    fun test_verify_exactly() {
        val thingOne = mockk<ThingOne>()
        val thingTwo = ThingTwo()
        justRun { thingOne.doSomethingOne() }
        thingTwo.doSomethingOneTwice(thingOne)
        verify(exactly = 2) { thingOne.doSomethingOne() }
        verify(inverse = true) { thingOne.doSomethingTwo() }
    }

    @Test
    fun test_verify_order() {
        val thingOne = spyk<ThingOne>()
        val thingTwo = spyk<ThingTwo>()
        thingTwo.doSomethingOneThenTwo(thingOne)
        verifyOrder {
            thingOne.doSomethingOne()
            thingOne.doSomethingTwo()
        }
    }

    @Test
    fun test_slots_added() {
        val slot1 = slot<Int>()
        every {
            mockedCalculator.add(
                capture(slot1),
                any()
            )
        } returns 600
        assertEquals(600, mockedCalculator.add(2, 3))
        assertTrue(slot1.isCaptured)
        assertEquals(2, slot1.captured)
    }

    @Test
    fun test_is_called() {
        val called = spyk<CallMe>()
        val holder = Holder(called)
        holder.call("Bob")
        verify { called.callMe(any()) }
    }

    @Test
    fun test_more_less() {
        every { mockedCalculator.add(more(5), more(5)) } returns 1
        every { mockedCalculator.add(less(5), less(5)) } returns 2
        assertEquals(1, mockedCalculator.add(66, 67))
        assertEquals(2, mockedCalculator.add(2, 3))
    }
}