package com.jastzeonic.coroutinesflowunittestdemo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import kotlin.Exception

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {

    //This must add for live data test.
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testScheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setUp() {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }


    @Test
    fun `check for how control flow goes`() {
        val viewModel = TestedViewModel()
        runTest {
            viewModel.demoCoroutinesRunInSingleThread()
        }
        assertEquals(Result.Success(true), viewModel.testStateFlow.value)

    }

    @Test
    fun `show how to test state flow`() {
        val viewModel = TestedViewModel()
        runTest {
            viewModel.demoHowToTestStateFlow()
        }
        assertEquals(Result.Success(true), viewModel.testStateFlow.value)

    }

    @Test
    fun `show how to test live data`() {
        val viewModel = TestedViewModel()
        runTest {
            viewModel.demoHowToTestLiveData()
        }
        assertEquals(Result.Success(true), viewModel.testLiveData.value)

    }

    @Test
    fun `show how to test shared flow`() {
        val viewModel = TestedViewModel()
        var result:Result<Boolean> = Result.Failed(Exception("did not received the result"))
        val scope = MainScope()
        val channel = Channel<Result<Boolean>>()
        scope.launch(UnconfinedTestDispatcher()) {
            viewModel.testSharedFlow.collect{
                channel.send(it)
            }
        }
        runTest {
            viewModel.demoHowToTestSharedFlow()
            result = channel.receive()

        }
        assertEquals(Result.Success(true), result)

    }

}