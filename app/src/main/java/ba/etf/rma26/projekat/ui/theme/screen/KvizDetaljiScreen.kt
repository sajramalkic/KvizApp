package ba.etf.rma26.projekat.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ba.etf.rma26.projekat.viewmodel.KvizDetaljiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KvizDetaljiScreen(
    idKviza: Int,
    nazivKviza: String,
    onBack: () -> Unit,
    kvizDetaljiViewModel: KvizDetaljiViewModel = viewModel()
) {
    val pitanja by kvizDetaljiViewModel.pitanja.collectAsState()
    val kvizTaken by kvizDetaljiViewModel.kvizTaken.collectAsState()
    val ukupniBodovi by kvizDetaljiViewModel.ukupniBodovi.collectAsState()
    val isLoading by kvizDetaljiViewModel.isLoading.collectAsState()
    val greska by kvizDetaljiViewModel.greska.collectAsState()

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            }

            greska?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            ukupniBodovi?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Ukupno bodova: $it",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (kvizTaken == null) {
                Button(
                    onClick = { kvizDetaljiViewModel.zapocniKviz(idKviza) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Započni kviz")
                }
            } else {
                Text(
                    text = "Kviz u toku",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    itemsIndexed(pitanja) { index, pitanje ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "${index + 1}. ${pitanje.tekstPitanja}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                pitanje.opcije.forEachIndexed { opcIndex, opcija ->
                                    OutlinedButton(
                                        onClick = {
                                            kvizDetaljiViewModel.postaviOdgovor(pitanje.id, opcIndex)
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(opcija)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}