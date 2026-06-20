package ba.etf.rma26.projekat.network

import ba.etf.rma26.projekat.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private var baseUrl: String = Constants.DEFAULT_BASE_URL
    private var apiKey: String? = null
    private var retrofit: Retrofit? = null
    private var service: ApiService? = null

    fun postaviBaseURL(baseUrl: String) {
        this.baseUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        retrofit = null
        service = null
    }

    fun postaviApiKey(apiKey: String?) {
        this.apiKey = apiKey
    }

    fun getApiKey(): String? = apiKey

    fun getService(): ApiService {
        if (service == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            service = retrofit!!.create(ApiService::class.java)
        }
        return service!!
    }
}