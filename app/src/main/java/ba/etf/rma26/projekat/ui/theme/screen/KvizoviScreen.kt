package ba.etf.rma26.projekat.ui.theme.screen


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
import ba.etf.rma26.projekat.ui.theme.components.KvizItemS3
import ba.etf.rma26.projekat.viewmodel.KvizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KvizoviScreen(
    kvizViewModel: KvizViewModel,
    onBack: () -> Unit,
    onKvizKliknut: (Int, String) -> Unit
) {
    val filtrirani by kvizViewModel.filtrirani.collectAsState()
    val odabraniFilter by kvizViewModel.odabraniFilter.collectAsState()
    val isLoading by kvizViewModel.isLoading.collectAsState()

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
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .testTag("listaKvizova"),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filtrirani) { kviz ->
                    KvizItemS3(
                        kviz = kviz,
                        onClick = { onKvizKliknut(kviz.id, kviz.naziv) }
                    )
                }
            }
        }
    }
}