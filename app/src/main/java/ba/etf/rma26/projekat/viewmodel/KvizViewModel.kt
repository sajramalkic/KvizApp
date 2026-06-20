package ba.etf.rma26.projekat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.etf.rma26.projekat.data.models.Grupa
import ba.etf.rma26.projekat.data.models.Kviz
import ba.etf.rma26.projekat.data.models.KvizTaken
import ba.etf.rma26.projekat.data.models.Predmet
import ba.etf.rma26.projekat.data.repositories.*
import ba.etf.rma26.projekat.domain.FiltrirajKvizoveUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

enum class FilterOpcija(val label: String) {
    SVI_MOJI("Svi moji kvizovi"),
    SVI("Svi kvizovi"),
    URADENI("Urađeni kvizovi"),
    BUDUCI("Budući kvizovi"),
    PROSLI_NEURADENI("Prošli kvizovi")
}

class KvizViewModel : ViewModel() {

    private val filtrirajKvizoveUseCase = FiltrirajKvizoveUseCase()

    private val _odabraniFilter = MutableStateFlow(FilterOpcija.SVI_MOJI)
    val odabraniFilter: StateFlow<FilterOpcija> = _odabraniFilter.asStateFlow()

    private val _filtrirani = MutableStateFlow<List<Kviz>>(emptyList())
    val filtrirani: StateFlow<List<Kviz>> = _filtrirani.asStateFlow()

    private val _pocetiKvizovi = MutableStateFlow<List<KvizTaken>>(emptyList())
    val pocetiKvizovi: StateFlow<List<KvizTaken>> = _pocetiKvizovi.asStateFlow()

    private val _uradeniKvizIds = MutableStateFlow<Set<Int>>(emptySet())
    val uradeniKvizIds: StateFlow<Set<Int>> = _uradeniKvizIds.asStateFlow()


    private val _brojKvizova = MutableStateFlow(0)
    val brojKvizova: StateFlow<Int> = _brojKvizova.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _greska = MutableStateFlow<String?>(null)
    val greska: StateFlow<String?> = _greska.asStateFlow()

    // Upis state
    private val _odabranaGodina = MutableStateFlow<Int?>(null)
    val odabranaGodina: StateFlow<Int?> = _odabranaGodina.asStateFlow()

    private val _odabraniPredmet = MutableStateFlow<Predmet?>(null)
    val odabraniPredmet: StateFlow<Predmet?> = _odabraniPredmet.asStateFlow()

    private val _odabranaGrupa = MutableStateFlow<Grupa?>(null)
    val odabranaGrupa: StateFlow<Grupa?> = _odabranaGrupa.asStateFlow()

    private val _sviPredmeti = MutableStateFlow<List<Predmet>>(emptyList())
    private val _upisaneGrupe = MutableStateFlow<List<Grupa>>(emptyList())

    private val _predmetiZaGodinu = MutableStateFlow<List<Predmet>>(emptyList())
    val predmetiZaGodinu: StateFlow<List<Predmet>> = _predmetiZaGodinu.asStateFlow()

    private val _grupeZaPredmet = MutableStateFlow<List<Grupa>>(emptyList())
    val grupeZaPredmet: StateFlow<List<Grupa>> = _grupeZaPredmet.asStateFlow()

    private val _dugmeEnabled = MutableStateFlow(false)
    val dugmeEnabled: StateFlow<Boolean> = _dugmeEnabled.asStateFlow()

    private val _upisUspjesan = MutableStateFlow(false)
    val upisUspjesan: StateFlow<Boolean> = _upisUspjesan.asStateFlow()

    private val _upisanNaziv = MutableStateFlow<String?>(null)
    val upisanNaziv: StateFlow<String?> = _upisanNaziv.asStateFlow()

    init {
        ucitajPocetnepodatke()
    }

    private fun ucitajPocetnepodatke() {
        viewModelScope.launch {
            _isLoading.value = true
            _greska.value = null
            try {
                _sviPredmeti.value = PredmetIGrupaRepository.getPredmeti()
                _upisaneGrupe.value = PredmetIGrupaRepository.getUpisaneGrupe()
                osvjeziListu()
            } catch (e: IOException) {
                _greska.value = "Nema internet konekcije. Provjerite da je backend pokrenut."
            } catch (e: Exception) {
                _greska.value = "Greška servera. Pokušajte ponovo."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun osvjeziPodatke() {
        viewModelScope.launch {
            _isLoading.value = true
            _greska.value = null
            try {
                _sviPredmeti.value = PredmetIGrupaRepository.getPredmeti()
                _upisaneGrupe.value = PredmetIGrupaRepository.getUpisaneGrupe()
                osvjeziListu()
            } catch (e: IOException) {
                _greska.value = "Nema internet konekcije. Provjerite da je backend pokrenut."
            } catch (e: Exception) {
                _greska.value = "Greška servera. Pokušajte ponovo."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setFilter(filter: FilterOpcija) {
        _odabraniFilter.value = filter
        viewModelScope.launch { osvjeziListu() }
    }

    private suspend fun osvjeziListu() {
        try {
            val rezultat = filtrirajKvizoveUseCase(_odabraniFilter.value)
            _pocetiKvizovi.value = rezultat.pocetiKvizovi
            _uradeniKvizIds.value = rezultat.uradeniKvizIds
            _filtrirani.value = rezultat.kvizovi
            _brojKvizova.value = rezultat.kvizovi.size
        } catch (e: IOException) {
            _greska.value = "Nema internet konekcije."
        } catch (e: Exception) {
            _greska.value = "Greška pri učitavanju kvizova."
        }
    }

    fun setGodina(godina: Int) {
        _odabranaGodina.value = godina
        _odabraniPredmet.value = null
        _odabranaGrupa.value = null
        _predmetiZaGodinu.value = _sviPredmeti.value.filter { it.godina == godina }
        _grupeZaPredmet.value = emptyList()
        azurirajDugme()
    }

    fun setPredmet(predmet: Predmet) {
        _odabraniPredmet.value = predmet
        _odabranaGrupa.value = null
        viewModelScope.launch {
            try {
                val sveGrupeZaPredmet = PredmetIGrupaRepository.getGrupeZaPredmet(predmet.id)
                val upisaniGrupaIds = _upisaneGrupe.value.map { it.id }.toSet()
                _grupeZaPredmet.value = sveGrupeZaPredmet.filter { it.id !in upisaniGrupaIds }
            } catch (e: IOException) {
                _greska.value = "Nema internet konekcije."
            } catch (e: Exception) {
                _greska.value = "Greška servera."
            }
        }
        azurirajDugme()
    }

    fun setGrupa(grupa: Grupa) {
        _odabranaGrupa.value = grupa
        azurirajDugme()
    }

    fun upisise() {
        val grupa = _odabranaGrupa.value ?: return
        val predmet = _odabraniPredmet.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _greska.value = null
            try {
                val uspjelo = PredmetIGrupaRepository.upisiUGrupu(grupa.id)
                if (uspjelo) {
                    _upisUspjesan.value = true
                    _upisanNaziv.value = "${predmet.naziv} - ${grupa.naziv}"
                    _upisaneGrupe.value = PredmetIGrupaRepository.getUpisaneGrupe()
                    _odabraniPredmet.value = null
                    _odabranaGrupa.value = null
                    _odabranaGodina.value?.let { setGodina(it) }
                    osvjeziListu()
                } else {
                    _greska.value = "Upis nije uspio. Grupa možda ne postoji."
                }
            } catch (e: IOException) {
                _greska.value = "Nema internet konekcije."
            } catch (e: Exception) {
                _greska.value = "Greška pri upisu."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetUpisUspjesan() {
        _upisUspjesan.value = false
        _upisanNaziv.value = null
    }
    fun resetGreska() { _greska.value = null }

    private fun azurirajDugme() {
        _dugmeEnabled.value = _odabranaGodina.value != null &&
                _odabraniPredmet.value != null &&
                _odabranaGrupa.value != null
    }
}