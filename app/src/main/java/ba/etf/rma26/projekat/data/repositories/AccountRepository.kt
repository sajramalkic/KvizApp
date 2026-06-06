package ba.etf.rma26.projekat.data.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AccountRepository {

    private val _hash = MutableStateFlow("")

    val hash: StateFlow<String> = _hash.asStateFlow()

    suspend fun postaviHash(acHash: String): Boolean {
        return if (acHash.isNotBlank()) {
            _hash.value = acHash.trim()
            true
        } else {
            false
        }
    }

    suspend fun getHash(): String = _hash.value
}