package ba.etf.rma26.projekat.ui.theme.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ba.etf.rma26.projekat.data.models.Kviz
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun KvizItemS3(
    kviz: Kviz,
    onClick: () -> Unit
) {
    val referentno = LocalDateTime.of(2021, 4, 10, 12, 0)
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun parseDate(str: String): LocalDateTime = try {
        LocalDateTime.parse(str.replace("Z", ""), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    } catch (e: Exception) { referentno }

    val pocetak = parseDate(kviz.datumPocetak)
    val kraj = parseDate(kviz.datumKraj)

    val (statusBoja, statusOpis, prikazaniDatum) = when {
        pocetak.isAfter(referentno) -> Triple(Color(0xFFFFEB3B), "Žuta", pocetak.format(formatter))
        referentno.isAfter(pocetak) && referentno.isBefore(kraj) ->
            Triple(Color(0xFF4CAF50), "Zelena", kraj.format(formatter))
        else -> Triple(Color(0xFFF44336), "Crvena", kraj.format(formatter))
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
                        .testTag("kviz_status_icon"),
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${kviz.trajanje} min",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}