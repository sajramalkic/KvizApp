package ba.etf.rma26.projekat.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import ba.etf.rma26.projekat.data.models.Kviz
import ba.etf.rma26.projekat.data.models.KvizTaken
import ba.etf.rma26.projekat.util.Constants
import ba.etf.rma26.projekat.util.DateUtil
import ba.etf.rma26.projekat.util.preostaloVrijeme

@Composable
fun KvizItem(
    kviz: Kviz,
    kvizTaken: KvizTaken? = null,
    jeUradjen: Boolean = false,
    onClick: () -> Unit = {}
) {
    val referentno = Constants.REFERENTNO_VRIJEME
    val formatter = DateUtil.prikazFormatter

    val pocetak = DateUtil.parse(kviz.datumPocetak)
    val kraj = DateUtil.parse(kviz.datumKraj)
    val datumRada = kvizTaken?.datumRada?.let { DateUtil.parse(it) }

    val jeAktivan = referentno.isAfter(pocetak) && referentno.isBefore(kraj) && !jeUradjen

    val (statusBoja, statusOpis, prikazaniDatum) = when {
        jeUradjen ->
            Triple(Constants.BOJA_PLAVA, "Plava", datumRada?.format(formatter) ?: "")
        jeAktivan ->
            Triple(Constants.BOJA_ZELENA, "Zelena", kraj.format(formatter))
        pocetak.isAfter(referentno) ->
            Triple(Constants.BOJA_ZUTA, "Žuta", pocetak.format(formatter))
        else ->
            Triple(Constants.BOJA_CRVENA, "Crvena", kraj.format(formatter))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("kviz_item_${kviz.naziv}")
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = kviz.nazivPredmeta ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
                Card(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.TopEnd)
                        .testTag("kviz_status_icon")
                        .semantics { contentDescription = statusOpis },
                    colors = CardDefaults.cardColors(containerColor = statusBoja),
                    shape = CircleShape
                ) {}
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = kviz.naziv, style = MaterialTheme.typography.bodyLarge)
                    Text(text = prikazaniDatum, style = MaterialTheme.typography.bodyMedium)

                    if (jeAktivan) {
                        Text(
                            text = preostaloVrijeme(kraj, referentno),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${kviz.trajanje} min",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (jeUradjen) {
                            Text(
                                text = "${kvizTaken?.osvojenBodovi ?: 0} bodova",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}