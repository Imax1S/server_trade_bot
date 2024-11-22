package com.ioline.common

import ru.tinkoff.piapi.contract.v1.Quotation
import java.math.BigDecimal

fun Quotation.toDouble(): Double {
    val bigDecimal =
        if (this.units == 0L && this.nano == 0) BigDecimal.ZERO else BigDecimal.valueOf(this.units)
            .add(BigDecimal.valueOf(this.nano.toLong(), 9))
    return bigDecimal.toDouble()
}