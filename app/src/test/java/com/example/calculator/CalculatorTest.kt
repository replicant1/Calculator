package com.example.calculator

import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
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

    @Test
    fun test_multiple_results() {
        val holder = mockk<Maker>()
        every { holder.result() } returns 5 andThen 10 andThen 15
        assertEquals(5, holder.result())
        assertEquals(10, holder.result())
        assertEquals(15, holder.result())
    }

    @Test
    fun test_returns_lambda() {
        every { mockedCalculator.add(any(), any()) } answers { arg<Int>(0) * arg<Int>(1) }
        assertEquals(35, mockedCalculator.add(7, 5))
    }

    @Test
    fun test_relaxed() {
        val adder = mockk<Adder>(relaxed = true)
        assertEquals(0, adder.getInt())
        assertEquals("", adder.getString())
        assertEquals(0, adder.add(1, 2))
    }

    @Test
    fun `test properties`() {
        val team = spyk(Team(10, true))
        every { team getProperty "speed" } returns 20
        assertEquals(20, team.speed) // 20, not 10
    }

    @Test
    fun test_returns_answer_with_side_effect() {
        var side_effect = 2
        every { mockedCalculator.add(any(), any()) } answers {
            side_effect = 9
            5
        }
        assertEquals(5, mockedCalculator.add(6, 7))
        assertEquals(9, side_effect)
    }

    @Test
    fun `test args to stubbed method`() {
        every { mockedCalculator.add(any(), any()) } answers {
            val numbers = args as List<Int>
            numbers.sum()
        }
        assertEquals(5, mockedCalculator.add(2, 3))
    }

    @Test
    fun `test verify any`() {
        every { mockedCalculator.add(any(), any()) } returns 0
        mockedCalculator.add(2, 3)
        verify { mockedCalculator.add(2, any()) }
    }

    var car1 = mockk<Car>()
    var car2 = mockk<Car>()

    @OverrideMockKs
    var pair = TrafficSystem() // car1 and car2 above get injected into pair.

    @Test
    fun `test inject dependencies`() {
        println("*** pair = $pair")
        println("*** pair.car1 = ${pair.car1}")
        println("*** pair.car2 = ${pair.car2}")
    }

    @Test
    fun `test sequence of results`() {
        every { mockedCalculator.add(any(), any()) } returns 10 andThen 20
        val result1 = mockedCalculator.add(0, 0)
        val result2 = mockedCalculator.add(0, 0)
        val result3 = mockedCalculator.add(0, 0)
        assertEquals(10, result1)
        assertEquals(20, result2)
        assertEquals(20, result3)
    }

    @Test
    fun `test private method invoked`() {
        val bob = spyk<Bob>(recordPrivateCalls = true)
        bob.doBob()
        verify { bob["privateMethod"]() }
        verify { bob.publicMethod() }
    }
}