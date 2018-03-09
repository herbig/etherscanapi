package com.michaelherbig.etherscanapi.model

import java.math.BigDecimal

data class PriceResponse(val result: Price?) : ABaseResponse()

data class Price(val ethbtc: BigDecimal?,
                 val ethbtc_timestamp: Long?,
                 val ethusd: BigDecimal?,
                 val ethusd_timestamp: Long?)