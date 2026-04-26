package ba.etfrma.kvizapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ba.etfrma.kvizapp.data.KvizStaticData
import ba.etfrma.kvizapp.ui.theme.components.KvizItem
import ba.etfrma.kvizapp.viewmodel.KvizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KvizoviScreen(
    kvizViewModel: KvizViewModel,
    onBack: () -> Unit
) {
    val filtrirani by kvizViewModel.filtrirani.collectAsState()
    val odabraniFilter by kvizViewModel.odabraniFilter.collectAsState()
    val referentno = KvizStaticData.getReferentnoVrijeme()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(odabraniFilter.label) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("listaKvizova"),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filtrirani) { kviz ->
                KvizItem(kviz = kviz, referentno = referentno)
            }
        }
    }
}
