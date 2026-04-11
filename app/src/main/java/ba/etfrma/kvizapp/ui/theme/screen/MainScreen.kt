package ba.etfrma.kvizapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ba.etfrma.kvizapp.data.KvizStaticData
import ba.etfrma.kvizapp.ui.theme.components.FilterDropdown
import ba.etfrma.kvizapp.ui.theme.components.KvizItem
import ba.etfrma.kvizapp.ui.theme.components.UpisForm
import ba.etfrma.kvizapp.viewmodel.KvizViewModel

@Composable
fun MainScreen(kvizViewModel: KvizViewModel = viewModel()) {
    val odabraniFilter by kvizViewModel.odabraniFilter.collectAsState()
    val filtrirani by kvizViewModel.filtrirani.collectAsState()

    val odabranaGodina by kvizViewModel.odabranaGodina.collectAsState()
    val odabraniPredmet by kvizViewModel.odabraniPredmet.collectAsState()
    val odabranaGrupa by kvizViewModel.odabranaGrupa.collectAsState()
    val predmetiZaGodinu by kvizViewModel.predmetiZaGodinu.collectAsState()
    val grupeZaPredmet by kvizViewModel.grupeZaPredmet.collectAsState()
    val upisUspjesan by kvizViewModel.upisUspjesan.collectAsState()

    val referentno = KvizStaticData.getReferentnoVrijeme()

    // Snackbar za potvrdu upisa
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(upisUspjesan) {
        if (upisUspjesan) {
            snackbarHostState.showSnackbar("Uspješno ste se upisali na predmet!")
            kvizViewModel.resetUpisUspjesan()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("listaKvizova"),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Forma za upis
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
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
            }

            // Filter dropdown
            item {
                FilterDropdown(
                    odabranaOpcija = odabraniFilter,
                    onOdabir = { kvizViewModel.setFilter(it) }
                )
            }

            // Lista kvizova
            items(filtrirani) { kviz ->
                KvizItem(kviz = kviz, referentno = referentno)
            }
        }
    }
}