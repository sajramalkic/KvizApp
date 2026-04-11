package ba.etfrma.kvizapp.data
import ba.etfrma.kvizapp.model.Grupa
object GrupaStaticData {

    private val sveGrupe: List<Grupa> = listOf(
        // RMA
        Grupa("Grupa 1", "RMA"),
        Grupa("Grupa 2", "RMA"),
        // DM
        Grupa("Grupa 1", "DM"),
        Grupa("Grupa 2", "DM"),
        // ASP
        Grupa("Grupa 1", "ASP"),
        Grupa("Grupa 2", "ASP"),
        // RPR
        Grupa("Grupa 1", "RPR"),
        Grupa("Grupa 2", "RPR"),
        // IM1
        Grupa("Grupa 1", "IM1"),
        Grupa("Grupa 2", "IM1"),
        // IM2
        Grupa("Grupa 1", "IM2"),
        Grupa("Grupa 2", "IM2"),
        // LAG
        Grupa("Grupa 1", "LAG"),
        Grupa("Grupa 2", "LAG"),
        // VIS
        Grupa("Grupa 1", "VIS"),
        Grupa("Grupa 2", "VIS"),
        // OS
        Grupa("Grupa 1", "OS"),
        Grupa("Grupa 2", "OS"),
        // MLTI
        Grupa("Grupa 1", "MLTI"),
        Grupa("Grupa 2", "MLTI"),
        // OE
        Grupa("Grupa 1", "OE"),
        Grupa("Grupa 2", "OE"),
        // UUP
        Grupa("Grupa 1", "UUP"),
        Grupa("Grupa 2", "UUP"),
        // IF1
        Grupa("Grupa 1", "IF1"),
        Grupa("Grupa 2", "IF1"),
        // TP
        Grupa("Grupa 1", "TP"),
        Grupa("Grupa 2", "TP"),
        // LD
        Grupa("Grupa 1", "LD"),
        Grupa("Grupa 2", "LD"),
        // RA
        Grupa("Grupa 1", "RA"),
        Grupa("Grupa 2", "RA"),
        // SP
        Grupa("Grupa 1", "SP"),
        Grupa("Grupa 2", "SP"),
        // NA
        Grupa("Grupa 1", "NA"),
        Grupa("Grupa 2", "NA"),
        // OOAD
        Grupa("Grupa 1", "OOAD"),
        Grupa("Grupa 2", "OOAD"),
        // AFJ
        Grupa("Grupa 1", "AFJ"),
        Grupa("Grupa 2", "AFJ"),
        // CCI
        Grupa("Grupa 1", "CCI"),
        Grupa("Grupa 2", "CCI"),
        // US
        Grupa("Grupa 1", "US"),
        Grupa("Grupa 2", "US"),
        // DPS
        Grupa("Grupa 1", "DPS"),
        Grupa("Grupa 2", "DPS"),
        // ORM
        Grupa("Grupa 1", "ORM"),
        Grupa("Grupa 2", "ORM"),
        // 3. godina
        Grupa("Grupa 1", "OIS"),
        Grupa("Grupa 2", "OIS"),
        Grupa("Grupa 1", "OOI"),
        Grupa("Grupa 2", "OOI"),
        Grupa("Grupa 1", "RG"),
        Grupa("Grupa 2", "RG"),
        Grupa("Grupa 1", "PJP"),
        Grupa("Grupa 2", "PJP"),
        Grupa("Grupa 1", "VVS"),
        Grupa("Grupa 2", "VVS"),
        Grupa("Grupa 1", "WT"),
        Grupa("Grupa 2", "WT"),
        Grupa("Grupa 1", "ARM"),
        Grupa("Grupa 2", "ARM"),
        Grupa("Grupa 1", "PIS"),
        Grupa("Grupa 2", "PIS"),
        Grupa("Grupa 1", "SI"),
        Grupa("Grupa 2", "SI"),
        Grupa("Grupa 1", "VI"),
        Grupa("Grupa 2", "VI"),
        // 4. godina
        Grupa("Grupa 1", "POOS"),
        Grupa("Grupa 2", "POOS"),
        Grupa("Grupa 1", "PRS"),
        Grupa("Grupa 2", "PRS"),
        Grupa("Grupa 1", "MU"),
        Grupa("Grupa 2", "MU"),
        Grupa("Grupa 1", "OR"),
        Grupa("Grupa 2", "OR"),
        Grupa("Grupa 1", "NASP"),
        Grupa("Grupa 2", "NASP"),
        Grupa("Grupa 1", "NOS"),
        Grupa("Grupa 2", "NOS"),
        Grupa("Grupa 1", "RV"),
        Grupa("Grupa 2", "RV"),
        Grupa("Grupa 1", "OI"),
        Grupa("Grupa 2", "OI"),
        Grupa("Grupa 1", "RM"),
        Grupa("Grupa 2", "RM"),
        Grupa("Grupa 1", "MMS"),
        Grupa("Grupa 2", "MMS"),
        Grupa("Grupa 1", "PNWT"),
        Grupa("Grupa 2", "PNWT"),
        Grupa("Grupa 1", "PPIS"),
        Grupa("Grupa 2", "PPIS"),
        // 5. godina
        Grupa("Grupa 1", "NSI"),
        Grupa("Grupa 2", "NSI"),
        Grupa("Grupa 1", "RSRV"),
        Grupa("Grupa 2", "RSRV"),
        Grupa("Grupa 1", "MPVI"),
        Grupa("Grupa 2", "MPVI"),
        Grupa("Grupa 1", "IPMIS"),
        Grupa("Grupa 2", "IPMIS"),
        Grupa("Grupa 1", "TS"),
        Grupa("Grupa 2", "TS"),
        Grupa("Grupa 1", "SPO"),
        Grupa("Grupa 2", "SPO"),
        Grupa("Grupa 1", "RAB"),
        Grupa("Grupa 2", "RAB")
    )

    fun getGrupaFromPredmet(nazivPredmeta: String): List<Grupa> {
        return sveGrupe.filter { it.nazivPredmeta == nazivPredmeta }
    }
}