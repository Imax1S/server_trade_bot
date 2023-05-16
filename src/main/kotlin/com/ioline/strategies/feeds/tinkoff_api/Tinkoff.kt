package com.ioline.strategies.feeds.tinkoff_api

import org.roboquant.common.Asset
import org.roboquant.common.AssetType
import org.roboquant.common.Exchange
import ru.tinkoff.piapi.core.InvestApi

internal object Tinkoff {
    /**
     * Get the available stocks
     */
    internal fun getAvailableStocks(api: InvestApi): Map<String, Asset> {
        val assets = api.instrumentsService.assetsSync
        val exchange = Exchange.getInstance("US")
        return assets.map { Asset(it.uid, AssetType.STOCK, exchange = exchange) }.associateBy { it.symbol }
    }
}