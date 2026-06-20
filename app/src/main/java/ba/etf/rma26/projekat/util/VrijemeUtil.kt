package ba.etf.rma26.projekat.util

import java.time.Duration
import java.time.LocalDateTime

fun preostaloVrijeme(kraj: LocalDateTime, referentno: LocalDateTime): String {
    val trajanje = Duration.between(referentno, kraj)
    if (trajanje.isNegative) return ""

    val dani = trajanje.toDays()
    val sati = trajanje.toHours() % 24

    return when {
        dani > 0 -> "Još $dani ${ako(dani, "dan", "dana", "dana")}"
        sati > 0 -> "Još $sati ${ako(sati, "sat", "sata", "sati")}"
        else -> "Manje od sat vremena"
    }
}

private fun ako(broj: Long, jedan: String, malo: String, vise: String): String {
    return when {
        broj == 1L -> jedan
        broj in 2..4 -> malo
        else -> vise
    }
}