package ba.etf.rma26.projekat.domain

import ba.etf.rma26.projekat.data.models.Kviz
import ba.etf.rma26.projekat.data.models.KvizTaken
import ba.etf.rma26.projekat.data.repositories.KvizRepository
import ba.etf.rma26.projekat.data.repositories.OdgovorRepository
import ba.etf.rma26.projekat.data.repositories.PitanjeKvizRepository
import ba.etf.rma26.projekat.data.repositories.TakeKvizRepository
import ba.etf.rma26.projekat.util.Constants
import ba.etf.rma26.projekat.util.DateUtil
import ba.etf.rma26.projekat.viewmodel.FilterOpcija

data class FiltriraniRezultat(
    val kvizovi: List<Kviz>,
    val pocetiKvizovi: List<KvizTaken>,
    val uradeniKvizIds: Set<Int>
)

class FiltrirajKvizoveUseCase {

    private suspend fun izracunajUradeneKvizove(pocetiKvizovi: List<KvizTaken>): Set<Int> {
        val uradeniIds = mutableSetOf<Int>()
        for (taken in pocetiKvizovi) {
            try {
                val pitanja = PitanjeKvizRepository.getPitanja(taken.idKviza)
                val odgovori = OdgovorRepository.getOdgovoriKviz(taken.idKviza)
                if (pitanja.isNotEmpty() && odgovori.size >= pitanja.size) {
                    uradeniIds.add(taken.idKviza)
                }
            } catch (e: Exception) {
                // ignoriraj pojedinačnu grešku, nastavi sa ostalim kvizovima
            }
        }
        return uradeniIds
    }

    suspend operator fun invoke(filter: FilterOpcija): FiltriraniRezultat {
        val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi() ?: emptyList()
        val uradeniIds = izracunajUradeneKvizove(pocetiKvizovi)
        val referentno = Constants.REFERENTNO_VRIJEME

        val lista = when (filter) {
            FilterOpcija.SVI_MOJI -> KvizRepository.getUpisani()
            FilterOpcija.SVI -> KvizRepository.getAll()
            FilterOpcija.URADENI -> KvizRepository.getUpisani().filter { it.id in uradeniIds }
            FilterOpcija.BUDUCI -> KvizRepository.getUpisani().filter {
                DateUtil.parse(it.datumPocetak).isAfter(referentno) && it.id !in uradeniIds
            }
            FilterOpcija.PROSLI_NEURADENI -> KvizRepository.getUpisani().filter {
                DateUtil.parse(it.datumKraj).isBefore(referentno) && it.id !in uradeniIds
            }
        }.sortedBy { it.datumPocetak }

        return FiltriraniRezultat(lista, pocetiKvizovi, uradeniIds)
    }
}