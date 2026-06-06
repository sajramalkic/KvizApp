package ba.etf.rma26.projekat.data.models

data class Pitanje(
    val id: Int,
    val idKviza: Int,
    val naziv: String,
    val tekstPitanja: String,
    val opcije: List<String>,
    val tacan: Int
)