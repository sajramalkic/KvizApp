package ba.etf.rma26.projekat.data.models

import com.google.gson.annotations.SerializedName

data class Kviz(
    val id: Int,
    val naziv: String,
    val idPredmeta: Int,
    val idGrupe: Int,
    @SerializedName("datumPocetka") val datumPocetak: String,
    val datumKraj: String,
    val trajanje: Int,
    val nazivPredmeta: String?,
    val nazivGrupe: String?
)