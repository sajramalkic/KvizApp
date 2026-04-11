package ba.etfrma.kvizapp.data

import ba.etfrma.kvizapp.model.Predmet
object PredmetStaticData {

    private val sviPredmeti: List<Predmet> = listOf(
        // 1. godina
        Predmet("IM1", 1),
        Predmet("IM2", 1),
        Predmet("LAG", 1),
        Predmet("VIS", 1),
        Predmet("OS", 1),
        Predmet("MLTI", 1),
        Predmet("OE", 1),
        Predmet("UUP", 1),
        Predmet("IF1", 1),
        Predmet("TP", 1),
        // 2. godina
        Predmet("DM", 2),
        Predmet("ASP", 2),
        Predmet("RPR", 2),
        Predmet("LD", 2),
        Predmet("RA", 2),
        Predmet("SP", 2),
        Predmet("NA", 2),
        Predmet("RMA", 2),
        Predmet("OOAD", 2),
        Predmet("AFJ", 2),
        Predmet("CCI", 2),
        Predmet("US", 2),
        Predmet("DPS", 2),
        Predmet("ORM", 2),
        // 3. godina
        Predmet("OIS", 3),
        Predmet("OOI", 3),
        Predmet("RG", 3),
        Predmet("PJP", 3),
        Predmet("VVS", 3),
        Predmet("WT", 3),
        Predmet("ARM", 3),
        Predmet("PIS", 3),
        Predmet("SI", 3),
        Predmet("VI", 3),
        // 4. godina
        Predmet("POOS", 4),
        Predmet("PRS", 4),
        Predmet("MU", 4),
        Predmet("OR", 4),
        Predmet("NASP", 4),
        Predmet("NOS", 4),
        Predmet("RV", 4),
        Predmet("OI", 4),
        Predmet("RM", 4),
        Predmet("MMS", 4),
        Predmet("PNWT", 4),
        Predmet("PPIS", 4),
        // 5. godina
        Predmet("NSI", 5),
        Predmet("RSRV", 5),
        Predmet("MPVI", 5),
        Predmet("IPMIS", 5),
        Predmet("TS", 5),
        Predmet("SPO", 5),
        Predmet("RAB", 5)
    )

    // Inicijalno upisani predmeti
    private val upisaniPredmeti: MutableList<Predmet> = mutableListOf(
        Predmet("RMA", 2),
        Predmet("DM", 2)
    )

    fun getAll(): List<Predmet> = sviPredmeti

    fun getUpisani(): List<Predmet> = upisaniPredmeti.toList()

    fun getNeupisani(): List<Predmet> {
        val upisaniNazivi = upisaniPredmeti.map { it.naziv }
        return sviPredmeti.filter { it.naziv !in upisaniNazivi }
    }

    fun upisise(predmet: Predmet) {
        if (upisaniPredmeti.none { it.naziv == predmet.naziv }) {
            upisaniPredmeti.add(predmet)
        }
    }
}