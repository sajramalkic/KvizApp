package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.models.KvizTaken
import ba.etf.rma26.projekat.network.ApiConfig

object TakeKvizRepository {

    suspend fun zapocniKviz(idKviza: Int): KvizTaken? {
        val hash = AccountRepository.getHash()
        if (hash.isEmpty()) return null
        return try {
            ApiConfig.getService().zapocniKviz(hash, idKviza, ApiConfig.getApiKey())
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) null else throw e
        }
    }

    suspend fun getPocetiKvizovi(): List<KvizTaken>? {
        val hash = AccountRepository.getHash()
        if (hash.isEmpty()) return null
        return try {
            ApiConfig.getService().getPocetiKvizovi(hash)
        } catch (e: java.io.EOFException) {
            emptyList()
        }
    }
}