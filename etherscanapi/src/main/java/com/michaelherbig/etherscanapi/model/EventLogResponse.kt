package com.michaelherbig.etherscanapi.model

data class EventLogResponse(val result: List<EventLog>?) : ABaseResponse()

data class EventLog(val address: String?,
                    val topics: List<String>?,
                    val data: String?,
                    val blockNumber: String?,
                    val timeStamp: String?,
                    val gasPrice: String?,
                    val gasUsed: String?,
                    val logIndex: String?,
                    val transactionHash: String?,
                    val transactionIndex: String?)