package com.ioline.common

import com.ioline.data.models.Instrument
import ru.tinkoff.piapi.contract.v1.InstrumentShort
import ru.tinkoff.piapi.contract.v1.Quotation
import ru.tinkoff.piapi.core.InvestApi
import java.math.BigDecimal

fun Quotation.toDouble(): Double {
    val bigDecimal =
        if (this.units == 0L && this.nano == 0) BigDecimal.ZERO else BigDecimal.valueOf(this.units)
            .add(BigDecimal.valueOf(this.nano.toLong(), 9))
    return bigDecimal.toDouble()
}

fun InstrumentShort.toInstrument(api: InvestApi): Instrument {
    val prices = api.marketDataService.getLastPricesSync(listOf(figi))
    return Instrument(
        classCode = classCode,
        figi = figi,
        first1dayCandleDate = first1DayCandleDate.toString(),
        first1minCandleDate = first1MinCandleDate.toString(),
        forIisFlag = forIisFlag,
        instrumentType = instrumentType,
        name = name,
        ticker = ticker,
        uid = uid,
        price = if (prices.isEmpty()) 0.0 else prices.last().price.toDouble()
    )
}