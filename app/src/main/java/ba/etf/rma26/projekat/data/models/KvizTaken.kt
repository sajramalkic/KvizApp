package ba.etf.rma26.projekat.data.models

data class KvizTaken(
    val id: Int,
    val hash: String,
    val idKviza: Int,
    val datumRada: String,
    val zavrsen: Boolean,
    val osvojenBodovi: Int
)