package com.pd.stopwatch.model


class ElapsedTimeCalculator(
    private val timestampProvider: TimestampProvider,
) {
    // подсчет прошедшего времени
    fun calculate(state: StopwatchState.Running): Long {
        val currentTimestamp = timestampProvider.getMilliseconds()
        val timePassedSinceStart = if (currentTimestamp > state.startTime) {
            currentTimestamp - state.startTime
        } else {
            0
        }
        return timePassedSinceStart + state.elapsedTime
    }
}