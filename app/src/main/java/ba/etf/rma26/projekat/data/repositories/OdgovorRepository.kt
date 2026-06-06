package ba.etf.rma26.projekat.data.repositories


import ba.etf.rma26.projekat.data.models.Odgovor
import ba.etf.rma26.projekat.data.models.OdgovorRequest

object OdgovorRepository {

    suspend fun getOdgovoriKviz(idKviza: Int): List<Odgovor> {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isEmpty()) return emptyList()
            ApiConfig.getService().getOdgovoriKviz(hash, idKviza)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun postaviOdgovorKviz(idKvizTaken: Int, idPitanje: Int, odgovor: Int): Int {
        return try {
            val hash = AccountRepository.getHash()
            if (hash.isEmpty()) return -1
            ApiConfig.getService().postaviOdgovor(
                hash,
                idKvizTaken,
                OdgovorRequest(idPitanje, odgovor),
                ApiConfig.getApiKey()
            )
        } catch (e: Exception) {
            -1
        }
    }
}