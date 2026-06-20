package ba.etf.rma26.projekat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ba.etf.rma26.projekat.data.repositories.AccountRepository
import java.io.IOException
class AuthViewModel : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentHash = MutableStateFlow<String?>(null)
    val currentHash: StateFlow<String?> = _currentHash.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _greska = MutableStateFlow<String?>(null)
    val greska: StateFlow<String?> = _greska.asStateFlow()

    fun login(hash: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _greska.value = null
            try {
                val uspjelo = AccountRepository.postaviHash(hash)
                if (uspjelo) {
                    _currentHash.value = hash
                    _isLoggedIn.value = true
                } else {
                    _greska.value = "Hash ne može biti prazan."
                }
            } catch (e: IOException) {
                _greska.value = "Nema internet konekcije."
            } catch (e: Exception) {
                _greska.value = "Greška servera."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun odjava() {
        viewModelScope.launch {
            AccountRepository.postaviHash("")  // resetuj i repository, ne samo lokalni state
        }
        _currentHash.value = null
        _isLoggedIn.value = false
    }
}