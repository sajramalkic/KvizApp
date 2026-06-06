package ba.etf.rma26.projekat.network


import ba.etf.rma26.projekat.data.models.Grupa
import ba.etf.rma26.projekat.data.models.Kviz
import ba.etf.rma26.projekat.data.models.KvizTaken
import ba.etf.rma26.projekat.data.models.Odgovor
import ba.etf.rma26.projekat.data.models.OdgovorRequest
import ba.etf.rma26.projekat.data.models.Pitanje
import ba.etf.rma26.projekat.data.models.Predmet
import retrofit2.http.*

interface ApiService {

    // Predmeti i grupe
    @GET("predmet")
    suspend fun getPredmeti(): List<Predmet>

    @GET("grupa")
    suspend fun getGrupe(): List<Grupa>

    @GET("predmet/{id}/grupa")
    suspend fun getGrupeZaPredmet(@Path("id") id: Int): List<Grupa>

    @GET("student/{hash}/grupa")
    suspend fun getUpisaneGrupe(@Path("hash") hash: String): List<Grupa>

    @POST("student/{hash}/grupa/{id}")
    suspend fun upisiUGrupu(
        @Path("hash") hash: String,
        @Path("id") id: Int,
        @Header("X-API-Key") apiKey: String? = null
    ): Boolean

    // Kvizovi
    @GET("kviz")
    suspend fun getAll(): List<Kviz>

    @GET("kviz/{id}")
    suspend fun getById(@Path("id") id: Int): Kviz

    @GET("student/{hash}/kviz")
    suspend fun getUpisani(@Path("hash") hash: String): List<Kviz>

    // Pitanja
    @GET("kviz/{id}/pitanja")
    suspend fun getPitanja(@Path("id") id: Int): List<Pitanje>

    // Pokušaji
    @POST("student/{hash}/kviz/{id}")
    suspend fun zapocniKviz(
        @Path("hash") hash: String,
        @Path("id") id: Int,
        @Header("X-API-Key") apiKey: String? = null
    ): KvizTaken

    @GET("student/{hash}/kviztaken")
    suspend fun getPocetiKvizovi(@Path("hash") hash: String): List<KvizTaken>?

    // Odgovori
    @GET("student/{hash}/kviz/{id}/odgovori")
    suspend fun getOdgovoriKviz(
        @Path("hash") hash: String,
        @Path("id") id: Int
    ): List<Odgovor>

    @POST("student/{hash}/kviztaken/{id}/odgovor")
    suspend fun postaviOdgovor(
        @Path("hash") hash: String,
        @Path("id") id: Int,
        @Body odgovor: OdgovorRequest,
        @Header("X-API-Key") apiKey: String? = null
    ): Int
}