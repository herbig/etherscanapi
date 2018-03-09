package com.michaelherbig.etherscanapi.model

import java.math.BigInteger

data class TransactionListResponse(val result: List<Transaction>?) : ABaseResponse()

data class Transaction(val blockNumber: Int?,
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
                       val isError: Int?, // values: 0=No Error, 1=Got Error)
                       val errDescription: String?,
                       val txreceipt_status: String?,
                       val input: String?,
                       val contractAddress: String?,
                       val cumulativeGasUsed: BigInteger?,
                       val gasUsed: BigInteger?,
                       val confirmations: Long?,
                       val traceId: String?,
                       val type: String?,
                       val errCode: String?)