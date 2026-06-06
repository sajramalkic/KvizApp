package ba.etf.rma26.projekat.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.etf.rma26.projekat.data.models.KvizTaken
import ba.etf.rma26.projekat.data.models.Pitanje
import ba.etf.rma26.projekat.data.repositories.OdgovorRepository
import ba.etf.rma26.projekat.data.repositories.PitanjeKvizRepository
import ba.etf.rma26.projekat.data.repositories.TakeKvizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    fun ucitajKviz(idKviza: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _pitanja.value = PitanjeKvizRepository.getPitanja(idKviza)
            } catch (e: Exception) {
                _greska.value = "Greška pri učitavanju pitanja"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun zapocniKviz(idKviza: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val taken = TakeKvizRepository.zapocniKviz(idKviza)
                _kvizTaken.value = taken
                if (taken != null) {
                    _pitanja.value = PitanjeKvizRepository.getPitanja(idKviza)
                }
            } catch (e: Exception) {
                _greska.value = "Greška pri započinjanju kviza"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun postaviOdgovor(idPitanje: Int, odgovor: Int) {
        val taken = _kvizTaken.value ?: return
        viewModelScope.launch {
            val bodovi = OdgovorRepository.postaviOdgovorKviz(taken.id, idPitanje, odgovor)
            if (bodovi >= 0) {
                _ukupniBodovi.value = bodovi
            } else {
                _greska.value = "Greška pri slanju odgovora"
            }
        }
    }
}