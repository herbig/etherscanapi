package com.michaelherbig.etherscanapi.model

import java.math.BigInteger

data class BalanceResponse(val result: BigInteger?) : ABaseResponse()