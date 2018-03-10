package com.michaelherbig.etherscanapi

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.michaelherbig.etherscanapi.model.*
import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.WebSocket
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import io.reactivex.disposables.Disposable


class Etherscan constructor(apiKey: String, chain: Chain = Chain.MAIN) {

    private interface EtherscanApi {

        //
        // Account related APIs: https://etherscan.io/apis#accounts
        //

        @GET("/api")
        fun getAccountBalance(@Query("module") module: String, @Query("action") action: String, @Query("address") address: String, @Query("tag") tag: String): Observable<BalanceResponse>

        @GET("/api")
        fun getAccountBalanceMulti(@Query("module") module: String, @Query("action") action: String, @Query("address") address: String, @Query("tag") tag: String): Observable<BalanceMultiResponse>

        @GET("/api")
        fun getTransactionList(@Query("module") module: String, @Query("action") action: String, @Query("address") address: String, @Query("startblock") startblock: String?, @Query("endblock") endblock: String?, @Query("sort") sort: String?, @Query("page") page: String?, @Query("offset") offset: String?): Observable<TransactionListResponse>

        @GET("/api")
        fun getInternalTransactionsByHash(@Query("module") module: String, @Query("action") action: String, @Query("txhash") txhash: String): Observable<TransactionListResponse>

        @GET("/api")
        fun getBlocksMinedByAddress(@Query("module") module: String, @Query("action") action: String, @Query("address") address: String, @Query("blocktype") blocktype: String, @Query("page") page: String?, @Query("offset") offset: String?): Observable<BlocksMinedResponse>

        //
        // Contract related APIs: https://etherscan.io/apis#contracts
        //

        @GET("/api")
        fun getContractABI(@Query("module") module: String, @Query("action") action: String, @Query("address") address: String): Observable<ContractABIResponse>

        //
        // Transaction related APIs: https://etherscan.io/apis#transactions
        //

        @GET("/api")
        fun getContractExecutionStatus(@Query("module") module: String, @Query("action") action: String, @Query("txhash") txhash: String): Observable<ExecutionStatusResponse>

        @GET("/api")
        fun getTransactionReceiptStatus(@Query("module") module: String, @Query("action") action: String, @Query("txhash") txhash: String): Observable<TransactionReceiptResponse>

        //
        // Blocks related APIs: https://etherscan.io/apis#blocks
        //

        @GET("/api")
        fun getBlockReward(@Query("module") module: String, @Query("action") action: String, @Query("blockno") blockno: Int): Observable<BlockRewardResponse>

        //
        // Event log related APIs: https://etherscan.io/apis#logs
        //

        @GET("/api")
        fun getEventLogs(@Query("module") module: String, @Query("action") action: String,
                         @Query("fromBlock") fromBlock: String, @Query("toBlock") toBlock: String, @Query("address") address: String,
                         @Query("topic0") topic0: String?, @Query("topic1") topic1: String?, @Query("topic2") topic2: String?, @Query("topic3") topic3: String?,
                         @Query("topic0_1_opr") topic0_1_opr: String?, @Query("topic1_2_opr") topic1_2_opr: String?, @Query("topic2_3_opr") topic2_3_opr: String?, @Query("topic0_2_opr") topic0_2_opr: String?
        ): Observable<EventLogResponse>

        //
        // Token related APIs: https://etherscan.io/apis#tokens
        //

        fun getERC20TotalSupply(@Query("module") module: String, @Query("action") action: String, @Query("contractaddress") contractaddress: String): Observable<BalanceResponse>

        fun getERC20Balance(@Query("module") module: String, @Query("action") action: String, @Query("contractaddress") contractaddress: String, @Query("address") address: String, @Query("tag") tag: String): Observable<BalanceResponse>

        //
        // Stats related APIs: https://etherscan.io/apis#stats
        //

        fun getEthTotalSupply(@Query("module") module: String, @Query("action") action: String): Observable<BalanceResponse>

        fun getEthLastPrice(@Query("module") module: String, @Query("action") action: String): Observable<PriceResponse>

    }

    enum class Chain(val url: String) {
        MAIN("https://api.etherscan.io"),
        RINKEBY("https://api-rinkeby.etherscan.io"),
        ROPSTEN("https://api-ropsten.etherscan.io"),
        KOVAN("https://api-kovan.etherscan.io"),
    }

    enum class Sort {
        ASC, DESC
    }

    enum class BlockType {
        BLOCKS, UNCLES
    }

    enum class TopicOperator {
        AND, OR
    }

    private val etherscanApi: EtherscanApi
    private val chain: Chain

    init {
        val client = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                })
                .addInterceptor { chain ->
                    val request = chain.request()
                    val url = request.url().newBuilder().addQueryParameter("apikey", apiKey).build()
                    chain.proceed(request.newBuilder().url(url).build())
                }
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(chain.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

        this.etherscanApi = retrofit.create(EtherscanApi::class.java)
        this.chain = chain
    }

    fun getAccountBalance(address: String): Observable<BalanceResponse> {
        return etherscanApi.getAccountBalance("account", "balance", address, "latest") // TODO what other "tags" are there???
    }

    fun getAccountBalances(vararg addresses: String): Observable<BalanceMultiResponse> {
        return etherscanApi.getAccountBalanceMulti("account", "balancemulti", addresses.joinToString(","), "latest")
    }

    fun getTransactionList(address: String, startblock: String = "", endblock: String = "", sort: Sort = Sort.ASC, page: String = "", offset: String = ""): Observable<TransactionListResponse> {
        return etherscanApi.getTransactionList("account", "txlist", address, startblock, endblock, sort.name.toLowerCase(), page, offset)
    }

    fun getInternalTransactionList(address: String, startblock: String? = null, endblock: String? = null, sort: Sort = Sort.ASC, page: String? = null, offset: String? = null): Observable<TransactionListResponse> {
        return etherscanApi.getTransactionList("account", "txlistinternal", address, startblock, endblock, sort.name.toLowerCase(), page, offset)
    }

    fun getInternalTransactionsByHash(txhash: String): Observable<TransactionListResponse> {
        return etherscanApi.getInternalTransactionsByHash("account", "txlistinternal", txhash)
    }

    fun getBlocksMinedByAddress(address: String, blocktype: BlockType = BlockType.BLOCKS, page: String? = null, offset: String? = null): Observable<BlocksMinedResponse> {
        return etherscanApi.getBlocksMinedByAddress("account", "getminedblocks", address, blocktype.name.toLowerCase(), page, offset)
    }

    fun getContractABI(address: String): Observable<ContractABIResponse> {
        return etherscanApi.getContractABI("contract", "getabi", address)
    }

    fun getExecutionStatus(txhash: String): Observable<ExecutionStatusResponse> {
        return etherscanApi.getContractExecutionStatus("transaction", "getstatus", txhash)
    }

    fun getTransactionReceiptStatus(txhash: String): Observable<TransactionReceiptResponse> {
        return etherscanApi.getTransactionReceiptStatus("transaction", "gettxreceiptstatus", txhash)
    }

    fun getBlockReward(blockno: Int): Observable<BlockRewardResponse> {
        return etherscanApi.getBlockReward("block", "getblockreward", blockno)
    }

    fun getEventLogs(fromBlock: String, toBlock: String, address: String, topic0: String? = null, topic1: String? = null, topic2: String? = null, topic3: String? = null, topic0_1_opr: TopicOperator? = null, topic1_2_opr: TopicOperator? = null, topic2_3_opr: TopicOperator? = null, topic0_2_opr: TopicOperator? = null): Observable<EventLogResponse> {
        return etherscanApi.getEventLogs("logs", "getLogs", fromBlock, toBlock, address, topic0, topic1, topic2, topic3, topic0_1_opr?.name?.toLowerCase(), topic1_2_opr?.name?.toLowerCase(), topic2_3_opr?.name?.toLowerCase(), topic0_2_opr?.name?.toLowerCase())
    }

    fun getERC20TotalSupply(contractaddress: String): Observable<BalanceResponse> {
        return etherscanApi.getERC20TotalSupply("stats", "tokensupply", contractaddress)
    }

    fun getERC20Balance(contractaddress: String, address: String): Observable<BalanceResponse> {
        return etherscanApi.getERC20Balance("account", "tokenbalance", contractaddress, address, "latest")
    }

    fun getEthTotalSupply(): Observable<BalanceResponse> {
        return etherscanApi.getEthTotalSupply("stats", "ethsupply")
    }

    fun getEthLastPrice(): Observable<PriceResponse> {
        return etherscanApi.getEthLastPrice("stats", "ethprice")
    }

    fun openWebSocket(address: String): Observable<WSResponse> {

        if (chain != Chain.MAIN) {
            throw UnsupportedOperationException("Testnet websockets are not yet supported by Etherscan.io")
        }

        return Observable.create<WSResponse> { emitter ->
            val socketListener = object : WebSocketListener() {

                private lateinit var timer: Disposable

                override fun onOpen(webSocket: WebSocket, response: Response) {
                    timer = Observable.interval(0, 50, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .doOnNext {
                                webSocket.send("{\"event\": \"ping\"}")
                            }.subscribe()
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    val response = Gson().fromJson(text, WSResponse::class.java)
                    if (response.event.equals("txlist")) {
                        emitter.onNext(response)
                    }
                }

                override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
                    webSocket?.close(1000, null)
                    timer.dispose()
                }

                override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
                    emitter.onComplete()
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    webSocket.close(1000, null)
                    timer.dispose()
                    emitter.onError(t)
                }
            }

            val client = OkHttpClient()
            val ws = client.newWebSocket(Request.Builder().url("wss://socket.etherscan.io/wshandler").build(), socketListener)
            ws.send("{\"event\": \"txlist\", \"address\": \"$address\"}")
            client.dispatcher().executorService().shutdown()
        }
    }

}