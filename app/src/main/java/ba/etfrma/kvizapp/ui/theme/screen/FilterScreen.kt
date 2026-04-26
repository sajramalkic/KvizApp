package ba.etfrma.kvizapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ba.etfrma.kvizapp.ui.theme.components.FilterDropdown
import ba.etfrma.kvizapp.ui.theme.components.UpisForm
import ba.etfrma.kvizapp.viewmodel.KvizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    kvizViewModel: KvizViewModel,
    onPrikaziKvizove: () -> Unit
) {
    val odabraniFilter by kvizViewModel.odabraniFilter.collectAsState()
    val odabranaGodina by kvizViewModel.odabranaGodina.collectAsState()
    val odabraniPredmet by kvizViewModel.odabraniPredmet.collectAsState()
    val odabranaGrupa by kvizViewModel.odabranaGrupa.collectAsState()
    val predmetiZaGodinu by kvizViewModel.predmetiZaGodinu.collectAsState()
    val grupeZaPredmet by kvizViewModel.grupeZaPredmet.collectAsState()
    val brojKvizova by kvizViewModel.brojKvizova.collectAsState()
    val upisUspjesan by kvizViewModel.upisUspjesan.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(upisUspjesan) {
        if (upisUspjesan) {
            snackbarHostState.showSnackbar("Uspješno ste se upisali na predmet!")
            kvizViewModel.resetUpisUspjesan()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Postavke") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            // Sekcija za upis
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                UpisForm(
                    odabranaGodina = odabranaGodina,
                    odabraniPredmet = odabraniPredmet,
                    odabranaGrupa = odabranaGrupa,
                    predmetiZaGodinu = predmetiZaGodinu,
                    grupeZaPredmet = grupeZaPredmet,
                    dugmeEnabled = kvizViewModel.dugmeEnabled,
                    onGodinaOdabrana = { kvizViewModel.setGodina(it) },
                    onPredmetOdabran = { kvizViewModel.setPredmet(it) },
                    onGrupaOdabrana = { kvizViewModel.setGrupa(it) },
                    onUpis = { kvizViewModel.upisise() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sekcija za filter
            Text(
                text = "Filteri",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            FilterDropdown(
                odabranaOpcija = odabraniFilter,
                onOdabir = { kvizViewModel.setFilter(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Broj kvizova
            Text(
                text = "Pronađeno je $brojKvizova kvizova",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .testTag("brojKvizova")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dugme za navigaciju
            Button(
                onClick = onPrikaziKvizove,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .testTag("prikaziKvizoveDugme")
            ) {
                Text("Prikaži kvizove")
            }
        }
    }
}