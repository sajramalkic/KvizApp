package ba.etf.rma26.projekat.util

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime


object Constants {

    // Referentno vrijeme korišteno za određivanje statusa kviza (vidi postavku Spirale 1)
    val REFERENTNO_VRIJEME: LocalDateTime = LocalDateTime.of(2021, 5, 20, 12, 0)

    const val DEFAULT_BASE_URL = "http://10.0.2.2:3000/"

    // Boje statusa kviza
    val BOJA_PLAVA = Color(0xFF2196F3)   // urađen
    val BOJA_ZELENA = Color(0xFF4CAF50)  // aktivan
    val BOJA_ZUTA = Color(0xFFFFEB3B)    // budući
    val BOJA_CRVENA = Color(0xFFF44336)  // prošao, neurađen
}