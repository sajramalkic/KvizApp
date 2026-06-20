package ba.etf.rma26.projekat.data.repositories

import ba.etf.rma26.projekat.data.models.Odgovor
import ba.etf.rma26.projekat.data.models.OdgovorRequest
import ba.etf.rma26.projekat.network.ApiConfig

object OdgovorRepository {

    suspend fun getOdgovoriKviz(idKviza: Int): List<Odgovor> {
        val hash = AccountRepository.getHash()
        if (hash.isEmpty()) return emptyList()
        return ApiConfig.getService().getOdgovoriKviz(hash, idKviza)
    }

    suspend fun postaviOdgovorKviz(idKvizTaken: Int, idPitanje: Int, odgovor: Int): Int {
        val hash = AccountRepository.getHash()
        if (hash.isEmpty()) return -1
        return ApiConfig.getService().postaviOdgovor(
            hash,
            idKvizTaken,
            OdgovorRequest(idPitanje, odgovor),
            ApiConfig.getApiKey()
        )
    }
}