package com.michaelherbig.etherscanapi.model

import java.math.BigInteger

data class BlockRewardResponse(val result: BlockReward?) : ABaseResponse()

data class BlockReward(val blockNumber: Int?,
                       val timeStamp: Long?,
                       val blockMiner: String?,
                       val blockReward: BigInteger?,
                       val uncles: List<Uncle>?)

data class Uncle(val miner: String?,
                 val unclePosition: Int?,
                 val blockreward: BigInteger?)