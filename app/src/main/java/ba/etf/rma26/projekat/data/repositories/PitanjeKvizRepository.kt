package ba.etf.rma26.projekat.data.repositories


import ba.etf.rma26.projekat.data.models.Pitanje

object PitanjeKvizRepository {

    suspend fun getPitanja(idKviza: Int): List<Pitanje> {
        return try {
            ApiConfig.getService().getPitanja(idKviza)
        } catch (e: Exception) {
            emptyList()
        }
    }
}