package com.example.calculator.marco

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import net.bytebuddy.pool.TypePool.Resolution.Illegal
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainPresenterTest {
    @RelaxedMockK
    lateinit var mockedView: View

    @RelaxedMockK
    lateinit var mockedRepository: DataRepository

    lateinit var mockedPresenter: Presenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockedPresenter = MainPresenter(mockedView, mockedRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test presenter fetching data sees that data returned to view`() {
        every { mockedRepository.fetchData() } returns emptyList()

        mockedPresenter.fetchData()

        val captureData = slot<List<UiDataModel>>()

        // View's "onResult" method must've been called once
        verify(exactly = 1) { mockedView.onResult(capture(captureData)) }
        captureData.captured.let { res ->
            assertNotNull(res)
            assert(res.isEmpty())
        }
    }

    @Test
    fun `test exception thrown when fetching data`() {
        every { mockedRepository.fetchData() } throws IllegalStateException("ouch!")

        mockedPresenter.fetchData() // Exception is caught and turned into a call on mockedZView.onError

        // Note: How to verify a method *isn't* called.
        verify(exactly = 0) { mockedView.onResult(any()) }
        verify(exactly = 1) { mockedView.onError(any()) }
    }

}