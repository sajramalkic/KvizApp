package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.models.Kviz
import ba.etf.rma26.projekat.network.ApiConfig
import ba.etf.rma26.projekat.network.ApiService

object KvizRepository {

    suspend fun getAll(): List<Kviz> {
        return ApiConfig.getService().getAll()
    }

    suspend fun getById(id: Int): Kviz? {
        return try {
            ApiConfig.getService().getById(id)
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) null else throw e
        }
    }

    suspend fun getUpisani(): List<Kviz> {
        val hash = AccountRepository.getHash()
        if (hash.isEmpty()) return emptyList()
        return ApiConfig.getService().getUpisani(hash)
    }
}