package com.ioline.strategies

import com.ioline.data.models.TimePeriod
import com.ioline.data.models.TimeSettings
import org.roboquant.common.*

object TimeSettingsParser {
    fun parse(timeSettings: TimeSettings?): TradingPeriod {
        val timeNumber = timeSettings?.start?.toIntOrNull() ?: 0
        return when (timeSettings?.period) {
            TimePeriod.MINUTES -> timeNumber.minutes
            TimePeriod.HOURS -> timeNumber.hours
            TimePeriod.DAYS -> timeNumber.days
            TimePeriod.WEEKS -> timeNumber.weeks
            TimePeriod.MONTHS -> timeNumber.months
            TimePeriod.YEARS -> timeNumber.years
            null -> throw IllegalArgumentException("Time settings are missing")
        }
    }
}