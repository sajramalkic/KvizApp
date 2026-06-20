package ba.etf.rma26.projekat.ui.theme.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ba.etf.rma26.projekat.viewmodel.KvizDetaljiViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KvizDetaljiScreen(
    idKviza: Int,
    nazivKviza: String,
    onBack: () -> Unit,
    onPromijeniGrupu: () -> Unit,
    kvizDetaljiViewModel: KvizDetaljiViewModel = viewModel()
) {
    val pitanja by kvizDetaljiViewModel.pitanja.collectAsState()
    val kvizTaken by kvizDetaljiViewModel.kvizTaken.collectAsState()
    val ukupniBodovi by kvizDetaljiViewModel.ukupniBodovi.collectAsState()
    val isLoading by kvizDetaljiViewModel.isLoading.collectAsState()
    val greska by kvizDetaljiViewModel.greska.collectAsState()
    val jePristupMoguc by kvizDetaljiViewModel.jePristupMoguc.collectAsState()
    val nijeNjegovaGrupa by kvizDetaljiViewModel.nijeNjegovaGrupa.collectAsState()
    val odgovoreniIds by kvizDetaljiViewModel.odgovoreniIds.collectAsState()
    val odabraniOdgovori by kvizDetaljiViewModel.odabraniOdgovori.collectAsState()
    val jeZavrsen by kvizDetaljiViewModel.jeZavrsen.collectAsState()
    val kvizJosNijePocelo by kvizDetaljiViewModel.kvizJosNijePocelo.collectAsState()
    val kvizJeIstekao by kvizDetaljiViewModel.kvizJeIstekao.collectAsState()

    LaunchedEffect(idKviza) {
        kvizDetaljiViewModel.ucitajKviz(idKviza)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(nazivKviza) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Nazad"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        // ── Ekran rezultata ──────────────────────────────────────────────
        if (jeZavrsen && kvizTaken != null) {
            val bodovi = ukupniBodovi ?: (kvizTaken?.osvojenBodovi ?: 0)
            val ukupnoPitanja = pitanja.size
            val procenat = if (ukupnoPitanja > 0)
                (odgovoreniIds.size.toFloat() / ukupnoPitanja * 100).roundToInt()
            else 0

            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Kviz završen!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Odgovorili ste na sva pitanja.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(32.dp))

                    // Rezultat kartica
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$bodovi",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "bodova",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(Modifier.height(16.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                            Spacer(Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$ukupnoPitanja",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "pitanja",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$procenat%",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "završeno",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Povratak na listu kvizova")
                    }
                }
            }
            return@Scaffold
        }

        // ── Glavni sadržaj ───────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            greska?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── Pristup onemogućen ───────────────────────────────────────
            if (!jePristupMoguc) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (nijeNjegovaGrupa) {
                            Text(
                                text = "Ovo nije kviz Vaše grupe.",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Upisani ste na ovaj predmet, ali u drugoj grupi. Promijenite grupu da biste pristupili ovom kvizu.",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = onPromijeniGrupu) {
                                Text("Promijeni grupu")
                            }
                        } else {
                            Text(
                                text = "Niste upisani na predmet ovog kviza.",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Upišite se na predmet da biste mogli pristupiti kvizu.",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = onPromijeniGrupu) {
                                Text("Idi na upis")
                            }
                        }
                    }
                }
                return@Column
            }

            // ── Kviz još nije počeo ──────────────────────────────────────
            if (kvizJosNijePocelo) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "Ovaj kviz još nije počeo. Vratite se kasnije kad bude aktivan.",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                return@Column
            }
            // ── Kviz je istekao ───────────────────────────────────────────
            if (kvizJeIstekao) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Rok za ovaj kviz je istekao. Niste ga uradili na vrijeme.",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                return@Column
            }

            // ── Kviz nije počet ──────────────────────────────────────────
            if (kvizTaken == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Kviz nije još počet.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = { kvizDetaljiViewModel.zapocniKviz(idKviza) },
                            modifier = Modifier.fillMaxWidth(0.7f)
                        ) {
                            Text("Započni kviz")
                        }
                    }
                }
                return@Column
            }

            // ── Kviz u toku ──────────────────────────────────────────────

            // Progress header
            val odgovoreno = odgovoreniIds.size
            val ukupno = pitanja.size
            val progressFraction = if (ukupno > 0) odgovoreno.toFloat() / ukupno else 0f

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pitanje $odgovoreno / $ukupno",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${(progressFraction * 100).roundToInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                    )
                }
            }

            // Lista pitanja
            val listState = rememberLazyListState()
            LazyColumn(state = listState) {
                itemsIndexed(pitanja) { index, pitanje ->
                    val odabraniIndex = odabraniOdgovori[pitanje.id]
                    val jeOdgovoreno = pitanje.id in odgovoreniIds

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "${index + 1}. ${pitanje.tekstPitanja}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                if (jeOdgovoreno) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = "Odgovoreno",
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(start = 4.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            pitanje.opcije.forEachIndexed { opcIndex, opcija ->
                                val jeOdabrano = odabraniIndex == opcIndex
                                if (jeOdabrano) {
                                    Button(
                                        onClick = {},
                                        enabled = false,
                                        colors = ButtonDefaults.buttonColors(
                                            disabledContainerColor = MaterialTheme.colorScheme.primary,
                                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    ) { Text(opcija) }
                                } else {
                                    OutlinedButton(
                                        onClick = {
                                            if (!jeOdgovoreno)
                                                kvizDetaljiViewModel.postaviOdgovor(pitanje.id, opcIndex)
                                        },
                                        enabled = !jeOdgovoreno,
                                        modifier = Modifier.fillMaxWidth()
                                    ) { Text(opcija) }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}