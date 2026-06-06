package ba.etf.rma26.projekat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.etf.rma26.projekat.data.models.Grupa
import ba.etf.rma26.projekat.data.models.Kviz
import ba.etf.rma26.projekat.data.models.Predmet
import ba.etf.rma26.projekat.data.repositories.AccountRepository
import ba.etf.rma26.projekat.data.repositories.KvizRepository
import ba.etf.rma26.projekat.data.repositories.PredmetIGrupaRepository
import ba.etf.rma26.projekat.data.repositories.TakeKvizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class FilterOpcija(val label: String) {
    SVI_MOJI("Svi moji kvizovi"),
    SVI("Svi kvizovi"),
    URADENI("Urađeni kvizovi"),
    BUDUCI("Budući kvizovi"),
    PROSLI_NEURADENI("Prošli kvizovi")
}

class KvizViewModel : ViewModel() {

    private val _odabraniFilter = MutableStateFlow(FilterOpcija.SVI_MOJI)
    val odabraniFilter: StateFlow<FilterOpcija> = _odabraniFilter.asStateFlow()

    private val _filtrirani = MutableStateFlow<List<Kviz>>(emptyList())
    val filtrirani: StateFlow<List<Kviz>> = _filtrirani.asStateFlow()

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

    init {
        viewModelScope.launch {
            AccountRepository.hash.collectLatest { hash ->
                ucitajPocetnepodatkeZaHash()
            }
        }
    }
    private suspend fun ucitajPocetnepodatkeZaHash() {
        _isLoading.value = true
        try {
            _sviPredmeti.value = PredmetIGrupaRepository.getPredmeti()
            _upisaneGrupe.value = PredmetIGrupaRepository.getUpisaneGrupe()
            osvjeziListu()
        } catch (e: Exception) {
            _greska.value = "Greška pri učitavanju podataka"
        } finally {
            _isLoading.value = false
        }
    }

    fun setFilter(filter: FilterOpcija) {
        _odabraniFilter.value = filter
        viewModelScope.launch { osvjeziListu() }
    }

    private suspend fun osvjeziListu() {
        val referentno = LocalDateTime.of(2021, 4, 10, 12, 0)
        val lista = when (_odabraniFilter.value) {
            FilterOpcija.SVI_MOJI -> KvizRepository.getUpisani()
            FilterOpcija.SVI -> KvizRepository.getAll()
            FilterOpcija.URADENI -> {
                val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi() ?: emptyList()
                val uradeniIds = pocetiKvizovi.map { it.idKviza }.toSet()
                KvizRepository.getUpisani().filter { it.id in uradeniIds }
            }
            FilterOpcija.BUDUCI -> {
                KvizRepository.getUpisani().filter {
                    val datum = LocalDateTime.parse(
                        it.datumPocetak.replace("Z", ""),
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    )
                    datum.isAfter(referentno)
                }
            }
            FilterOpcija.PROSLI_NEURADENI -> {
                val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi() ?: emptyList()
                val uradeniIds = pocetiKvizovi.map { it.idKviza }.toSet()
                KvizRepository.getUpisani().filter {
                    val datum = LocalDateTime.parse(
                        it.datumKraj.replace("Z", ""),
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    )
                    datum.isBefore(referentno) && it.id !in uradeniIds
                }
            }
        }.sortedBy { it.datumPocetak }
        _filtrirani.value = lista
        _brojKvizova.value = lista.size
    }

    fun setGodina(godina: Int) {
        _odabranaGodina.value = godina
        _odabraniPredmet.value = null
        _odabranaGrupa.value = null
        val upisaniPredmetiIds = _upisaneGrupe.value.map { it.idPredmeta }.toSet()
        _predmetiZaGodinu.value = _sviPredmeti.value
            .filter { it.godina == godina && it.id !in upisaniPredmetiIds }
        _grupeZaPredmet.value = emptyList()
        azurirajDugme()
    }

    fun setPredmet(predmet: Predmet) {
        _odabraniPredmet.value = predmet
        _odabranaGrupa.value = null
        viewModelScope.launch {
            _grupeZaPredmet.value = PredmetIGrupaRepository.getGrupeZaPredmet(predmet.id)
        }
        azurirajDugme()
    }

    fun setGrupa(grupa: Grupa) {
        _odabranaGrupa.value = grupa
        azurirajDugme()
    }

    fun upisise() {
        val grupa = _odabranaGrupa.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val uspjelo = PredmetIGrupaRepository.upisiUGrupu(grupa.id)
            if (uspjelo) {
                _upisUspjesan.value = true
                _upisaneGrupe.value = PredmetIGrupaRepository.getUpisaneGrupe()
                _odabraniPredmet.value = null
                _odabranaGrupa.value = null
                _odabranaGodina.value?.let { setGodina(it) }
                osvjeziListu()
            } else {
                _greska.value = "Upis nije uspio"
            }
            _isLoading.value = false
        }
    }

    fun resetUpisUspjesan() { _upisUspjesan.value = false }
    fun resetGreska() { _greska.value = null }

    private fun azurirajDugme() {
        _dugmeEnabled.value = _odabranaGodina.value != null &&
                _odabraniPredmet.value != null &&
                _odabranaGrupa.value != null
    }
}