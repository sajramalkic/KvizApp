package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.models.Pitanje
import ba.etf.rma26.projekat.network.ApiConfig

object PitanjeKvizRepository {

    suspend fun getPitanja(idKviza: Int): List<Pitanje> {
        return ApiConfig.getService().getPitanja(idKviza)
    }
}