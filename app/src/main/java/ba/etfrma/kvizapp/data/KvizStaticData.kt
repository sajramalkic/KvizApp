package ba.etfrma.kvizapp.data
import ba.etfrma.kvizapp.model.Kviz
import java.util.Date
import java.time.LocalDate
import java.time.LocalDateTime
object KvizStaticData {


    private val referentnoVrijeme = LocalDateTime.of(2021, 4, 10, 12, 0)

    private val sviKvizovi: MutableList<Kviz> = mutableListOf(


        Kviz(
            naziv = "RMA Kviz 1",
            nazivPredmeta = "RMA",
            datumPocetak = LocalDateTime.of(2021, 4, 1, 8, 0),
            datumKraj = LocalDateTime.of(2021, 4, 5, 23, 59),
            datumRada = LocalDateTime.of(2021, 4, 3, 10, 0),
            trajanje = 2,
            nazivGrupe = "Grupa 1",
            osvojeniBodovi = 1.5f
        ),

        Kviz(
            naziv = "RMA Kviz 2",
            nazivPredmeta = "RMA",
            datumPocetak = LocalDateTime.of(2021, 4, 8, 8, 0),
            datumKraj = LocalDateTime.of(2021, 4, 15, 23, 59),
            datumRada = null,
            trajanje = 2,
            nazivGrupe = "Grupa 1",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "RMA Kviz 3",
            nazivPredmeta = "RMA",
            datumPocetak = LocalDateTime.of(2021, 5, 10, 8, 0),
            datumKraj = LocalDateTime.of(2021, 5, 17, 23, 59),
            datumRada = null,
            trajanje = 5,
            nazivGrupe = "Grupa 1",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "RMA Kviz 0",
            nazivPredmeta = "RMA",
            datumPocetak = LocalDateTime.of(2021, 3, 1, 8, 0),
            datumKraj = LocalDateTime.of(2021, 3, 10, 23, 59),
            datumRada = null,
            trajanje = 2,
            nazivGrupe = "Grupa 1",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "DM Kviz 1",
            nazivPredmeta = "DM",
            datumPocetak = LocalDateTime.of(2021, 3, 5, 8, 0),
            datumKraj = LocalDateTime.of(2021, 3, 12, 23, 59),
            datumRada = LocalDateTime.of(2021, 3, 10, 9, 0),
            trajanje = 5,
            nazivGrupe = "Grupa 1",
            osvojeniBodovi = 2.5f
        ),


        Kviz(
            naziv = "ASP Kviz 1",
            nazivPredmeta = "ASP",
            datumPocetak = LocalDateTime.of(2021, 4, 20, 8, 0),
            datumKraj = LocalDateTime.of(2021, 4, 27, 23, 59),
            datumRada = null,
            trajanje = 10,
            nazivGrupe = "Grupa 1",
            osvojeniBodovi = null
        ),
        Kviz(
            naziv = "ASP Kviz 1",
            nazivPredmeta = "ASP",
            datumPocetak = LocalDateTime.of(2021, 4, 20, 8, 0),
            datumKraj = LocalDateTime.of(2021, 4, 27, 23, 59),
            datumRada = null,
            trajanje = 10,
            nazivGrupe = "Grupa 2",
            osvojeniBodovi = null
        ),


        Kviz(
            naziv = "RPR Kviz 1",
            nazivPredmeta = "RPR",
            datumPocetak = LocalDateTime.of(2021, 4, 12, 8, 0),
            datumKraj = LocalDateTime.of(2021, 4, 19, 23, 59),
            datumRada = null,
            trajanje = 8,
            nazivGrupe = "Grupa 1",
            osvojeniBodovi = null
        ),
        Kviz(
            naziv = "RPR Kviz 1",
            nazivPredmeta = "RPR",
            datumPocetak = LocalDateTime.of(2021, 4, 12, 8, 0),
            datumKraj = LocalDateTime.of(2021, 4, 19, 23, 59),
            datumRada = null,
            trajanje = 8,
            nazivGrupe = "Grupa 2",
            osvojeniBodovi = null
        ),

        Kviz(
            naziv = "IM1 Kviz 1",
            nazivPredmeta = "IM1",
            datumPocetak = LocalDateTime.of(2021, 4, 15, 8, 0),
            datumKraj = LocalDateTime.of(2021, 4, 22, 23, 59),
            datumRada = null,
            trajanje = 6,
            nazivGrupe = "Grupa 1",
            osvojeniBodovi = null
        ),
        Kviz(
            naziv = "IM1 Kviz 1",
            nazivPredmeta = "IM1",
            datumPocetak = LocalDateTime.of(2021, 4, 15, 8, 0),
            datumKraj = LocalDateTime.of(2021, 4, 22, 23, 59),
            datumRada = null,
            trajanje = 6,
            nazivGrupe = "Grupa 2",
            osvojeniBodovi = null
        )
    )
    fun getReferentDate(): LocalDate {
        return referentnoVrijeme.toLocalDate()
    }

    fun getReferentnoVrijeme(): LocalDateTime = referentnoVrijeme

    fun getAll(): List<Kviz> = sviKvizovi.sortedBy { it.datumPocetak }

    fun getUpisani(): List<Kviz> {
        val upisaniPredmeti = PredmetStaticData.getUpisani().map { it.naziv }
        return sviKvizovi
            .filter { it.nazivPredmeta in upisaniPredmeti }
            .sortedBy { it.datumPocetak }
    }

    fun getDone(): List<Kviz> {
        return getUpisani().filter { it.datumRada != null }
    }

    fun getFuture(): List<Kviz> {
        return getUpisani().filter { it.datumPocetak.isAfter(referentnoVrijeme) }
    }

    fun getNotTaken(): List<Kviz> {
        return getUpisani().filter {
            it.datumKraj.isBefore(referentnoVrijeme) && it.datumRada == null
        }
    }

}