package tossaro.android.core.example.data.network

import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query
import tossaro.android.core.example.data.network.response.ErrorResponse
import tossaro.android.core.example.data.network.response.StockResponse

interface StockServiceV1 {
    @GET("/data/top/totaltoptiervolfull")
    suspend fun stocks(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("tsym") tsym: String
    ): NetworkResponse<StockResponse, ErrorResponse>
}