package ba.etf.rma26.projekat.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtil {

    private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val prikazFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun parse(str: String, fallback: LocalDateTime = Constants.REFERENTNO_VRIJEME): LocalDateTime {
        return try {
            LocalDateTime.parse(str.replace("Z", "").take(19), isoFormatter)
        } catch (e: Exception) {
            fallback
        }
    }
}