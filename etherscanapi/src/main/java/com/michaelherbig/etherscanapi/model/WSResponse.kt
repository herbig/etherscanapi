package com.michaelherbig.etherscanapi.model

import java.math.BigInteger

data class WSResponse(val event: String?,
                      val address: String?,
                      val result: List<Event>)

data class Event(val blockNumber: Int?,
                 val timeStamp: Long?,
                 val hash: String?,
                 val nonce: String?,
                 val blockHash: String?,
                 val transactionIndex: Int?,
                 val from: String?,
                 val to: String?,
                 val value: BigInteger?,
                 val gas: BigInteger?,
                 val gasPrice: BigInteger?,
                 val input: String?,
                 val contractAddress: String?,
                 val cumulativeGasUsed: BigInteger?,
                 val gasUsed: BigInteger?,
                 val confirmations: Int?)