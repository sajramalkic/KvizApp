package ba.etf.rma26.projekat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.etf.rma26.projekat.data.models.KvizTaken
import ba.etf.rma26.projekat.data.models.Pitanje
import ba.etf.rma26.projekat.data.repositories.KvizRepository
import ba.etf.rma26.projekat.data.repositories.OdgovorRepository
import ba.etf.rma26.projekat.data.repositories.PitanjeKvizRepository
import ba.etf.rma26.projekat.data.repositories.TakeKvizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.io.IOException
import ba.etf.rma26.projekat.util.DateUtil
import ba.etf.rma26.projekat.util.Constants

class KvizDetaljiViewModel : ViewModel() {

    private val _pitanja = MutableStateFlow<List<Pitanje>>(emptyList())
    val pitanja: StateFlow<List<Pitanje>> = _pitanja.asStateFlow()

    private val _kvizTaken = MutableStateFlow<KvizTaken?>(null)
    val kvizTaken: StateFlow<KvizTaken?> = _kvizTaken.asStateFlow()

    private val _ukupniBodovi = MutableStateFlow<Int?>(null)
    val ukupniBodovi: StateFlow<Int?> = _ukupniBodovi.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _greska = MutableStateFlow<String?>(null)
    val greska: StateFlow<String?> = _greska.asStateFlow()

    private val _jePristupMoguc = MutableStateFlow(true)
    val jePristupMoguc: StateFlow<Boolean> = _jePristupMoguc.asStateFlow()

    private val _nijeNjegovaGrupa = MutableStateFlow(false)
    val nijeNjegovaGrupa: StateFlow<Boolean> = _nijeNjegovaGrupa.asStateFlow()

    private val _odgovoreniIds = MutableStateFlow<Set<Int>>(emptySet())
    val odgovoreniIds: StateFlow<Set<Int>> = _odgovoreniIds.asStateFlow()

    private val _odabraniOdgovori = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val odabraniOdgovori: StateFlow<Map<Int, Int>> = _odabraniOdgovori.asStateFlow()

    private val _kvizJosNijePocelo = MutableStateFlow(false)
    val kvizJosNijePocelo: StateFlow<Boolean> = _kvizJosNijePocelo.asStateFlow()

    private val _kvizJeIstekao = MutableStateFlow(false)
    val kvizJeIstekao: StateFlow<Boolean> = _kvizJeIstekao.asStateFlow()

    val jeZavrsen: StateFlow<Boolean> = combine(
        _odgovoreniIds, _pitanja
    ) { odgovoreni, pitanja ->
        pitanja.isNotEmpty() && odgovoreni.size >= pitanja.size
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun ucitajKviz(idKviza: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _greska.value = null
            try {
                val upisaniKvizovi = KvizRepository.getUpisani()
                val jeUpisanNaOvajKviz = upisaniKvizovi.any { it.id == idKviza }
                _jePristupMoguc.value = jeUpisanNaOvajKviz

                if (jeUpisanNaOvajKviz) {
                    val kviz = upisaniKvizovi.find { it.id == idKviza }
                    val pocetak = kviz?.let { DateUtil.parse(it.datumPocetak) }
                    val kraj = kviz?.let { DateUtil.parse(it.datumKraj) }

                    // Prvo provjeri da li je kviz već započet — to ima prednost nad
                    // provjerom datuma, jer korisnik mora moći dovršiti ono što je počeo
                    val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi() ?: emptyList()
                    val taken = pocetiKvizovi.find { it.idKviza == idKviza }

                    val jeBuduci = pocetak != null && pocetak.isAfter(Constants.REFERENTNO_VRIJEME)
                    val jeIstekao = kraj != null && kraj.isBefore(Constants.REFERENTNO_VRIJEME)

                    _kvizJosNijePocelo.value = jeBuduci && taken == null
                    _kvizJeIstekao.value = jeIstekao && taken == null

                    val blokiran = _kvizJosNijePocelo.value || _kvizJeIstekao.value

                    if (!blokiran) {
                        _pitanja.value = PitanjeKvizRepository.getPitanja(idKviza)
                        _kvizTaken.value = taken

                        if (taken != null) {
                            val odgovori = OdgovorRepository.getOdgovoriKviz(idKviza)
                            _odgovoreniIds.value = odgovori.map { it.idPitanje }.toSet()
                            _odabraniOdgovori.value = odgovori.associate { it.idPitanje to it.odgovor }
                        }
                    }
                } else {
                    val kviz = KvizRepository.getById(idKviza)
                    val jePredmetUpisan = kviz != null && upisaniKvizovi.any { it.idPredmeta == kviz.idPredmeta }
                    _nijeNjegovaGrupa.value = jePredmetUpisan
                }
            } catch (e: IOException) {
                _greska.value = "Nema internet konekcije."
            } catch (e: Exception) {
                _greska.value = "Greška pri učitavanju pitanja."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun zapocniKviz(idKviza: Int) {
        if (!_jePristupMoguc.value) {
            _greska.value = "Nemate pristup ovom kvizu."
            return
        }
        if (_kvizJosNijePocelo.value) {
            _greska.value = "Kviz još nije počeo."
            return
        }
        if (_kvizJeIstekao.value) {
            _greska.value = "Rok za ovaj kviz je istekao."
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _greska.value = null
            try {
                val taken = TakeKvizRepository.zapocniKviz(idKviza)
                _kvizTaken.value = taken
                if (taken != null) {
                    _pitanja.value = PitanjeKvizRepository.getPitanja(idKviza)
                }
            } catch (e: IOException) {
                _greska.value = "Nema internet konekcije."
            } catch (e: Exception) {
                _greska.value = "Greška pri započinjanju kviza."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun postaviOdgovor(idPitanje: Int, odgovor: Int) {
        val taken = _kvizTaken.value ?: return
        viewModelScope.launch {
            try {
                val bodovi = OdgovorRepository.postaviOdgovorKviz(taken.id, idPitanje, odgovor)
                if (bodovi >= 0) {
                    _ukupniBodovi.value = bodovi
                    _odgovoreniIds.value = _odgovoreniIds.value + idPitanje
                    _odabraniOdgovori.value = _odabraniOdgovori.value + (idPitanje to odgovor)
                } else {
                    _greska.value = "Neispravan zahtjev pri slanju odgovora."
                }
            } catch (e: IOException) {
                _greska.value = "Nema internet konekcije."
            } catch (e: Exception) {
                _greska.value = "Greška pri slanju odgovora."
            }
        }
    }
}