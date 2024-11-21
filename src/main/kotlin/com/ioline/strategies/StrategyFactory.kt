package com.ioline.strategies

import com.ioline.data.models.StrategyType
import org.roboquant.strategies.EMAStrategy
import org.roboquant.strategies.Strategy
import org.roboquant.ta.RSIStrategy
import org.roboquant.ta.TaLibStrategy

class StrategyFactory {
    fun createStrategy(type: StrategyType, param1: Double, param2: Double): Strategy {
        return when (type) {
            StrategyType.EMA -> {
                EMAStrategy(param1.toInt(), param2.toInt())
            }

            StrategyType.RSI -> {
                RSIStrategy(param1, param2)
            }

            StrategyType.CUSTOM -> TaLibStrategy()
        }
    }
}