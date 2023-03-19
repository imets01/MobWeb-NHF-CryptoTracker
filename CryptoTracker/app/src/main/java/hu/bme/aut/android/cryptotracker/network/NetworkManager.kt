package hu.bme.aut.android.cryptotracker.network

import hu.bme.aut.android.cryptotracker.model.MarketModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {

    private const val SERVICE_URL = "https://api.coinmarketcap.com/"
    private const val APP_ID = "ae7c0187-a6da-44c5-a1bd-0b199b095239"

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SERVICE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val cryptoApi: CryptoApi = retrofit.create(CryptoApi::class.java)

    suspend fun getCrypto(): Response<MarketModel> {
        return cryptoApi.getData()
    }

    fun getCryptoWithName(crypto: String?): Call<MarketModel?>? {
        return cryptoApi.getDataWithName(APP_ID,crypto)
    }
}