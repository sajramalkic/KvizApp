package ba.etfrma.kvizapp.viewmodel
import androidx.lifecycle.ViewModel
import ba.etfrma.kvizapp.data.GrupaStaticData
import ba.etfrma.kvizapp.data.KvizStaticData
import ba.etfrma.kvizapp.data.PredmetStaticData
import ba.etfrma.kvizapp.model.Grupa
import ba.etfrma.kvizapp.model.Kviz
import ba.etfrma.kvizapp.model.Predmet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // Upis state
    private val _odabranaGodina = MutableStateFlow<Int?>(null)
    val odabranaGodina: StateFlow<Int?> = _odabranaGodina.asStateFlow()

    private val _odabraniPredmet = MutableStateFlow<Predmet?>(null)
    val odabraniPredmet: StateFlow<Predmet?> = _odabraniPredmet.asStateFlow()

    private val _odabranaGrupa = MutableStateFlow<Grupa?>(null)
    val odabranaGrupa: StateFlow<Grupa?> = _odabranaGrupa.asStateFlow()

    private val _predmetiZaGodinu = MutableStateFlow<List<Predmet>>(emptyList())
    val predmetiZaGodinu: StateFlow<List<Predmet>> = _predmetiZaGodinu.asStateFlow()

    private val _grupeZaPredmet = MutableStateFlow<List<Grupa>>(emptyList())
    val grupeZaPredmet: StateFlow<List<Grupa>> = _grupeZaPredmet.asStateFlow()

    private val _upisUspjesan = MutableStateFlow(false)
    val upisUspjesan: StateFlow<Boolean> = _upisUspjesan.asStateFlow()

    init {
        osvjeziListu()
    }

    fun setFilter(filter: FilterOpcija) {
        _odabraniFilter.value = filter
        osvjeziListu()
    }

    private fun osvjeziListu() {
        val lista = when (_odabraniFilter.value) {
            FilterOpcija.SVI_MOJI -> KvizStaticData.getUpisani()
            FilterOpcija.SVI -> KvizStaticData.getAll()
            FilterOpcija.URADENI -> KvizStaticData.getDone()
            FilterOpcija.BUDUCI -> KvizStaticData.getFuture()
            FilterOpcija.PROSLI_NEURADENI -> KvizStaticData.getNotTaken()
        }
        _filtrirani.value = lista
        _brojKvizova.value = lista.size
    }

    fun setGodina(godina: Int) {
        _odabranaGodina.value = godina
        _odabraniPredmet.value = null
        _odabranaGrupa.value = null
        _predmetiZaGodinu.value = PredmetStaticData.getNeupisani()
            .filter { it.godina == godina }
        _grupeZaPredmet.value = emptyList()
    }

    fun setPredmet(predmet: Predmet) {
        _odabraniPredmet.value = predmet
        _odabranaGrupa.value = null
        _grupeZaPredmet.value = GrupaStaticData.getGrupaFromPredmet(predmet.naziv)
    }

    fun setGrupa(grupa: Grupa) {
        _odabranaGrupa.value = grupa
    }

    fun upisise() {
        val predmet = _odabraniPredmet.value ?: return
        PredmetStaticData.upisise(predmet)
        _upisUspjesan.value = true
        _odabraniPredmet.value = null
        _odabranaGrupa.value = null
        _predmetiZaGodinu.value = PredmetStaticData.getNeupisani()
            .filter { it.godina == _odabranaGodina.value }
        _grupeZaPredmet.value = emptyList()
        osvjeziListu()
    }

    fun resetUpisUspjesan() {
        _upisUspjesan.value = false
    }

    val dugmeEnabled: Boolean
        get() = _odabranaGodina.value != null &&
                _odabraniPredmet.value != null &&
                _odabranaGrupa.value != null
}