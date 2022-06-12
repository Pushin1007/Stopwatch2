package com.pd.stopwatch.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Di {
    val stopwatchModule = module {
        single<TimestampProvider> { TimestampProviderImp() }
        single { TimestampMillisecondsFormatter() }
        single { ElapsedTimeCalculator(timestampProvider = get()) }
        single {
            StopwatchStateCalculator(
                timestampProvider = get(),
                elapsedTimeCalculator = get()
            )
        }
        single {
            StopwatchStateHolder(
                elapsedTimeCalculator = get(),
                stopwatchStateCalculator = get(),
                timestampMillisecondsFormatter = get()
            )
        }
        single<StopwatchListOrchestrator> {
            StopwatchListOrchestrator(
                stopwatchStateHolder = get(), scope = CoroutineScope(
                    Dispatchers.Main
                            + SupervisorJob()
                )
            )
        }
        viewModel { MainViewModel(stopwatchListOrchestrator = get()) }
    }
}