package com.pd.stopwatch.model

interface TimestampProvider {
    fun getMilliseconds(): Long
}