package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.models.Grupa
import ba.etf.rma26.projekat.data.models.Predmet

object PredmetIGrupaRepository {

    suspend fun getPredmeti(): List<Predmet> {
        return try {
            ApiConfig.getService().getPredmeti()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getGrupe(): List<Grupa> {
        return try {
            ApiConfig.getService().getGrupe()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getGrupeZaPredmet(idPredmeta: Int): List<Grupa> {
        return try {
            ApiConfig.getService().getGrupeZaPredmet(idPredmeta)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun upisiUGrupu(idGrupa: Int): Boolean {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isEmpty()) return false
            ApiConfig.getService().upisiUGrupu(hash, idGrupa, ApiConfig.getApiKey())
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUpisaneGrupe(): List<Grupa> {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isEmpty()) return emptyList()
            ApiConfig.getService().getUpisaneGrupe(hash)
        } catch (e: Exception) {
            emptyList()
        }
    }
}