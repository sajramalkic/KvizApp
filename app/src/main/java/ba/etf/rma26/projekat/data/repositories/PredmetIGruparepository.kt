package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.models.Grupa
import ba.etf.rma26.projekat.data.models.Predmet
import ba.etf.rma26.projekat.network.ApiConfig

object PredmetIGrupaRepository {

    suspend fun getPredmeti(): List<Predmet> {
        return ApiConfig.getService().getPredmeti()
    }

    suspend fun getGrupe(): List<Grupa> {
        return ApiConfig.getService().getGrupe()
    }

    suspend fun getGrupeZaPredmet(idPredmeta: Int): List<Grupa> {
        return ApiConfig.getService().getGrupeZaPredmet(idPredmeta)
    }

    suspend fun upisiUGrupu(idGrupa: Int): Boolean {
        val hash = AccountRepository.getHash()
        if (hash.isEmpty()) return false
        return ApiConfig.getService().upisiUGrupu(hash, idGrupa, ApiConfig.getApiKey())
    }

    suspend fun getUpisaneGrupe(): List<Grupa> {
        val hash = AccountRepository.getHash()
        if (hash.isEmpty()) return emptyList()
        return ApiConfig.getService().getUpisaneGrupe(hash)
    }
}