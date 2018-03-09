package com.michaelherbig.etherscanapi.model

import java.math.BigInteger

data class BalanceMultiResponse(val result: List<Balance>?) : ABaseResponse()

data class Balance(val account: String?,
                   val balance: BigInteger?)