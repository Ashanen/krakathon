package com.castorama.krakathon.core.testing

import com.castorama.krakathon.core.usecase.UseCaseDispatchers
import io.kotest.core.listeners.AfterTestListener
import io.kotest.core.listeners.BeforeTestListener
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Swaps Main + use case dispatchers for a test dispatcher.
 * In a spec: `extension(TestDispatchersExtension())`.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatchersExtension(
    val dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : BeforeTestListener, AfterTestListener {

    override suspend fun beforeTest(testCase: TestCase) {
        Dispatchers.setMain(dispatcher)
        UseCaseDispatchers.setBackground(dispatcher)
    }

    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        Dispatchers.resetMain()
        UseCaseDispatchers.resetBackground()
    }
}
