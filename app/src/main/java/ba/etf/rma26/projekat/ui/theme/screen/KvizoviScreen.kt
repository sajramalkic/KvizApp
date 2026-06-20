package ba.etf.rma26.projekat.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import ba.etf.rma26.projekat.ui.theme.components.EmptyState
import ba.etf.rma26.projekat.ui.theme.components.KvizItem
import ba.etf.rma26.projekat.viewmodel.KvizViewModel
import ba.etf.rma26.projekat.viewmodel.AuthViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Login
import ba.etf.rma26.projekat.ui.theme.components.FilterDropdown
import ba.etf.rma26.projekat.viewmodel.FilterOpcija

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun KvizoviScreen(
    kvizViewModel: KvizViewModel,
    authViewModel: AuthViewModel,
    onKvizKliknut: (Int, String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val filtrirani by kvizViewModel.filtrirani.collectAsState()
    val odabraniFilter by kvizViewModel.odabraniFilter.collectAsState()
    val isLoading by kvizViewModel.isLoading.collectAsState()
    val pocetiKvizovi by kvizViewModel.pocetiKvizovi.collectAsState()
    val uradeniKvizIds by kvizViewModel.uradeniKvizIds.collectAsState()
    val greska by kvizViewModel.greska.collectAsState()
    var showOdjavaDialog by remember { mutableStateOf(false) }
    val currentHash by authViewModel.currentHash.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val prikazaniKvizovi = remember(filtrirani, searchQuery) {
        if (searchQuery.isBlank()) filtrirani
        else filtrirani.filter {
            it.naziv.contains(searchQuery, ignoreCase = true) ||
                    (it.nazivPredmeta?.contains(searchQuery, ignoreCase = true) ?: false)
        }
    }
    // ─────────────────────────────────────────────────────────

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                kvizViewModel.osvjeziPodatke()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(odabraniFilter.label) },

                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = if (currentHash != null)
                                Icons.Default.AccountCircle
                            else
                                Icons.Default.Person,
                            contentDescription = "Profil",
                            tint = if (currentHash != null)
                                MaterialTheme.colorScheme.primary
                            else
                                LocalContentColor.current
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        if (currentHash != null) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = currentHash!!,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = {},
                                enabled = false
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Odjavi se") },
                                leadingIcon = {
                                    Icon(Icons.Default.Logout, contentDescription = null)
                                },
                                onClick = {
                                    showMenu = false
                                    showOdjavaDialog = true
                                }
                            )
                        } else {
                            DropdownMenuItem(
                                text = { Text("Prijavi se") },
                                leadingIcon = {
                                    Icon(Icons.Default.Login, contentDescription = null)
                                },
                                onClick = {
                                    onNavigateToLogin()
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        val pullState = rememberPullRefreshState(
            refreshing = isLoading,
            onRefresh = { kvizViewModel.osvjeziPodatke() }
        )
        if (showOdjavaDialog) {
            AlertDialog(
                onDismissRequest = { showOdjavaDialog = false },
                title = { Text("Odjava") },
                text = { Text("Da li ste sigurni da se želite odjaviti?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showOdjavaDialog = false
                            authViewModel.odjava()
                        }
                    ) {
                        Text("Odjavi se", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showOdjavaDialog = false }) {
                        Text("Otkaži")
                    }
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullState)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                FilterDropdown(
                    odabranaOpcija = odabraniFilter,
                    onOdabir = { kvizViewModel.setFilter(it) },

                )
                greska?.let { porukaGreske ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = porukaGreske,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    kvizViewModel.resetGreska()
                                    kvizViewModel.osvjeziPodatke()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Pokušaj ponovo")
                            }
                        }
                    }
                }
                // ── search bar ────────────────────────────────
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Pretraži kvizove...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Obriši")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                // ─────────────────────────────────────────────

                if (prikazaniKvizovi.isEmpty() && !isLoading) {
                    EmptyState()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("listaKvizova"),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(prikazaniKvizovi) { kviz ->   // ← prikazaniKvizovi
                            val taken = pocetiKvizovi.find { it.idKviza == kviz.id }
                            val jeUradjen = kviz.id in uradeniKvizIds
                            KvizItem(
                                kviz = kviz,
                                kvizTaken = taken,
                                jeUradjen = jeUradjen,
                                onClick = { onKvizKliknut(kviz.id, kviz.naziv) }
                            )
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}