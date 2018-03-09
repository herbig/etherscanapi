package com.michaelherbig.etherscanapi.model

import java.math.BigInteger

data class BlocksMinedResponse(val result: List<Block>?) : ABaseResponse()

data class Block(val blockNumber: Int?,
                 val timeStamp: Long?,
                 val blockReward: BigInteger?)