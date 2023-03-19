package hu.bme.aut.android.cryptotracker.network

import retrofit2.Call
import hu.bme.aut.android.cryptotracker.model.MarketModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CryptoApi {
    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=100")
    suspend fun getData() : Response<MarketModel>

    @GET("v1/cryptocurrency/info")
    fun getDataWithName(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("symbol") symbol: String?
    ): Call<MarketModel?>?
}
