package com.michaelherbig.etherscanapi.model

data class ExecutionStatusResponse(val result: ExecutionStatus?) : ABaseResponse()

data class ExecutionStatus(val isError: Int?,
                           val errDescription: String?)