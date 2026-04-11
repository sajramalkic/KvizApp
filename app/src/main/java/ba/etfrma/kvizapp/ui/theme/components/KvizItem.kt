package ba.etfrma.kvizapp.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ba.etfrma.kvizapp.R
import ba.etfrma.kvizapp.model.Kviz
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class KvizStatus {
    PLAVA, ZELENA, ZUTA, CRVENA
}

fun odreditiStatus(kviz: Kviz, referentno: LocalDateTime): KvizStatus {
    return when {
        kviz.datumRada != null -> KvizStatus.PLAVA
        referentno.isAfter(kviz.datumPocetak) && referentno.isBefore(kviz.datumKraj) -> KvizStatus.ZELENA
        referentno.isBefore(kviz.datumPocetak) -> KvizStatus.ZUTA
        else -> KvizStatus.CRVENA
    }
}

fun odreditiDatum(kviz: Kviz, status: KvizStatus): LocalDateTime? {
    return when (status) {
        KvizStatus.PLAVA -> kviz.datumRada
        KvizStatus.ZELENA, KvizStatus.CRVENA -> kviz.datumKraj
        KvizStatus.ZUTA -> kviz.datumPocetak
    }
}

@Composable
fun KvizItem(kviz: Kviz, referentno: LocalDateTime) {
    val status = odreditiStatus(kviz, referentno)
    val datum = odreditiDatum(kviz, status)
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val (statusResId, statusOpis) = when (status) {
        KvizStatus.PLAVA -> Pair(R.drawable.plava, "Plava")
        KvizStatus.ZELENA -> Pair(R.drawable.zelena, "Zelena")
        KvizStatus.ZUTA -> Pair(R.drawable.zuta, "Žuta")
        KvizStatus.CRVENA -> Pair(R.drawable.crvena, "Crvena")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("kviz_item_${kviz.naziv}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header sa nazivom predmeta i statusom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = kviz.nazivPredmeta,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
                Image(
                    painter = painterResource(id = statusResId),
                    contentDescription = statusOpis,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.TopEnd)
                        .testTag("kviz_status_icon")
                )
            }

            // Detalji kviza
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = kviz.naziv,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    datum?.let {
                        Text(
                            text = it.format(formatter),
                            style = MaterialTheme.typography.bodyMedium
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
                        kviz.osvojeniBodovi?.let {
                            Text(
                                text = it.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}