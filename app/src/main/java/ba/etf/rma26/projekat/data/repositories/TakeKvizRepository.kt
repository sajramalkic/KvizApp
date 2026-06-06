package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.models.KvizTaken

object TakeKvizRepository {

    suspend fun zapocniKviz(idKviza: Int): KvizTaken? {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isEmpty()) return null
            ApiConfig.getService().zapocniKviz(hash, idKviza, ApiConfig.getApiKey())
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getPocetiKvizovi(): List<KvizTaken>? {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isEmpty()) return null
            ApiConfig.getService().getPocetiKvizovi(hash)
        } catch (e: Exception) {
            null
        }
    }
}