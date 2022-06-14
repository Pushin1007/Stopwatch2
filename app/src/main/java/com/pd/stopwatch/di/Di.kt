package com.pd.stopwatch.di

import com.pd.stopwatch.*
import com.pd.stopwatch.model.*
import com.pd.stopwatch.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object Di {
    val stopwatchModule = module {
        single<TimestampProvider>(qualifier = named(TIME_STAMP_PROVIDER)) { TimestampProviderImp() }
        single(qualifier = named(TIME_STAMP_MILLISECONDS_FORMATTER)) { TimestampMillisecondsFormatter() }
        single(qualifier = named(ELAPSED_TIME_CALCULATOR)) {
            ElapsedTimeCalculator(get(named(TIME_STAMP_PROVIDER)))
        }


        single(qualifier = named(STOPWATCH_STATE_CALCULATOR)) {
            StopwatchStateCalculator(
                get(named(TIME_STAMP_PROVIDER)),
                get(named(ELAPSED_TIME_CALCULATOR))
            )
        }
        single(qualifier = named(STOPWATCH_STATE_HOLDER)) {
            StopwatchStateHolder(
                get(named(STOPWATCH_STATE_CALCULATOR)),
                get(named(ELAPSED_TIME_CALCULATOR)),
                get(named(TIME_STAMP_MILLISECONDS_FORMATTER))
            )
        }
        single<StopwatchListOrchestrator>(qualifier = named(STOPWATCH_LIST_ORCHESTRATOR)) {
            StopwatchListOrchestrator(
                get(named(STOPWATCH_STATE_HOLDER)),
                CoroutineScope(Dispatchers.IO + SupervisorJob())
            )
        }
        viewModel(qualifier = named(MAIN_VIEW_MODEL)) {
            MainViewModel(get(named(STOPWATCH_LIST_ORCHESTRATOR)))
        }
    }
}
