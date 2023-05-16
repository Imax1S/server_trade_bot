package com.ioline.strategies.feeds.tinkoff_api

import com.ioline.common.sandboxToken
import com.ioline.common.toDouble
import org.roboquant.common.*
import org.roboquant.common.Currency
import org.roboquant.feeds.HistoricPriceFeed
import org.roboquant.feeds.PriceBar
import ru.tinkoff.piapi.contract.v1.CandleInterval
import ru.tinkoff.piapi.contract.v1.HistoricCandle
import ru.tinkoff.piapi.core.InvestApi
import ru.tinkoff.piapi.core.MarketDataService
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


class TinkoffFeedHistoricFeed(private val adjClose: Boolean = true) : HistoricPriceFeed() {
    val tinkoffApi = InvestApi.create(sandboxToken)

    private val stockData: MarketDataService
    private val logger = Logging.getLogger(TinkoffFeedHistoricFeed::class)
    private val zoneId: ZoneId = ZoneId.of("Europe/Moscow")


    init {
        stockData = tinkoffApi.marketDataService
    }

    private val availableStocks: Map<String, Asset> by lazy {
        Tinkoff.getAvailableStocks(tinkoffApi)
    }

    private fun getAsset(symbol: String) = availableStocks[symbol]

    private val Instant.zonedDateTime
        get() = ZonedDateTime.ofInstant(this, zoneId)

    private fun validateStockSymbols(symbols: Array<out String>) {
        require(symbols.isNotEmpty()) { "specify at least one symbol" }
        symbols.forEach {
            require(availableStocks[it] != null) { "unknown symbol $it" }
        }
    }

    fun retrieve(
        figis: List<String>,
        timeframe: Timeframe = Timeframe.past(1.years),
        interval: CandleInterval = CandleInterval.CANDLE_INTERVAL_DAY
    ) {
        val start = timeframe.start
        val end = timeframe.end
        figis.forEach { ticker ->
            val asset = Asset(ticker, AssetType.STOCK, Currency.RUB)
            val candles = stockData.getCandlesSync(ticker, start, end, interval)
            handle(asset, candles)
            assets.add(asset)
        }
    }

    private fun handle(asset: Asset, quotes: List<HistoricCandle>) {

        quotes.forEach {
            val action = PriceBar(
                asset,
                it.open.toDouble(),
                it.high.toDouble(),
                it.low.toDouble(),
                it.close.toDouble(),
                it.volume
            )
//            if (adjClose) action.adjustClose(it.adjClose)
            val now =  Instant.ofEpochSecond(it.time.seconds, it.time.nanos.toLong())
            add(now, action)
        }

        logger.info { "Received data for $asset" }
        logger.info { "Total ${timeline.size} steps from ${timeline.first()} to ${timeline.last()}" }
    }
}