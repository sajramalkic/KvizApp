package ba.etf.rma26.projekat.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ba.etf.rma26.projekat.ui.theme.components.FilterDropdown
import ba.etf.rma26.projekat.ui.theme.components.UpisForm
import ba.etf.rma26.projekat.viewmodel.KvizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    kvizViewModel: KvizViewModel
) {
    val odabranaGodina by kvizViewModel.odabranaGodina.collectAsState()
    val odabraniPredmet by kvizViewModel.odabraniPredmet.collectAsState()
    val odabranaGrupa by kvizViewModel.odabranaGrupa.collectAsState()
    val predmetiZaGodinu by kvizViewModel.predmetiZaGodinu.collectAsState()
    val grupeZaPredmet by kvizViewModel.grupeZaPredmet.collectAsState()
    val upisUspjesan by kvizViewModel.upisUspjesan.collectAsState()
    val upisanNaziv by kvizViewModel.upisanNaziv.collectAsState()
    val dugmeEnabled by kvizViewModel.dugmeEnabled.collectAsState()
    val isLoading by kvizViewModel.isLoading.collectAsState()
    val greska by kvizViewModel.greska.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(upisUspjesan) {
        if (upisUspjesan) {
            val poruka = if (upisanNaziv != null)
                "Uspješno ste upisani na $upisanNaziv"
            else
                "Uspješno ste se upisali na predmet!"
            snackbarHostState.showSnackbar(poruka)
            kvizViewModel.resetUpisUspjesan()
        }
    }

    LaunchedEffect(greska) {
        greska?.let {
            snackbarHostState.showSnackbar(it)
            kvizViewModel.resetGreska()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            // Upis kartica
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Upis na predmet",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    UpisForm(
                        odabranaGodina = odabranaGodina,
                        odabraniPredmet = odabraniPredmet,
                        odabranaGrupa = odabranaGrupa,
                        predmetiZaGodinu = predmetiZaGodinu,
                        grupeZaPredmet = grupeZaPredmet,
                        dugmeEnabled = dugmeEnabled,
                        onGodinaOdabrana = { kvizViewModel.setGodina(it) },
                        onPredmetOdabran = { kvizViewModel.setPredmet(it) },
                        onGrupaOdabrana = { kvizViewModel.setGrupa(it) },
                        onUpis = { kvizViewModel.upisise() }
                    )
                }
            }


        }
    }
}