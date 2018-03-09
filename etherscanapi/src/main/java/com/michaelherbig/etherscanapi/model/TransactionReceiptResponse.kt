package com.michaelherbig.etherscanapi.model

data class TransactionReceiptResponse(val result: TransactionReceipt?) : ABaseResponse()

data class TransactionReceipt(val status: Int?) // status: 0 = Fail, 1 = Pass. Will return null/empty value for pre-byzantium fork