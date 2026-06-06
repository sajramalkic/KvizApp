package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.models.Kviz

object KvizRepository {

    suspend fun getAll(): List<Kviz> {
        return try {
            ApiConfig.getService().getAll()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getById(id: Int): Kviz? {
        return try {
            ApiConfig.getService().getById(id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUpisani(): List<Kviz> {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isEmpty()) return emptyList()
            ApiConfig.getService().getUpisani(hash)
        } catch (e: Exception) {
            emptyList()
        }
    }
}