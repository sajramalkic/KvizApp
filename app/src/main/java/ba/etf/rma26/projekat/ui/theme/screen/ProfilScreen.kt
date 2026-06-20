package ba.etf.rma26.projekat.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ba.etf.rma26.projekat.viewmodel.AuthViewModel
import ba.etf.rma26.projekat.viewmodel.KvizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilScreen(
    authViewModel: AuthViewModel,
    kvizViewModel: KvizViewModel,
    onOdjava: () -> Unit
) {
    val currentHash by authViewModel.currentHash.collectAsState()
    val uradeniKvizIds by kvizViewModel.uradeniKvizIds.collectAsState()
    val pocetiKvizovi by kvizViewModel.pocetiKvizovi.collectAsState()
    val filtrirani by kvizViewModel.filtrirani.collectAsState()
    var showOdjavaDialog by remember { mutableStateOf(false) }
    val inicijali = currentHash?.take(2)?.uppercase() ?: "??"

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profil") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Avatar
            Surface(
                modifier = Modifier.size(88.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = inicijali,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentHash ?: "Nepoznat korisnik",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Student",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    broj = filtrirani.size.toString(),
                    label = "Dostupnih kvizova"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    broj = (pocetiKvizovi.size - uradeniKvizIds.size).toString(), // ← samo nezavršeni
                    label = "U toku"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    broj = uradeniKvizIds.size.toString(),
                    label = "Završenih"
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Odjava
            OutlinedButton(
                onClick = { showOdjavaDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Odjavi se")
            }

            if (showOdjavaDialog) {
                AlertDialog(
                    onDismissRequest = { showOdjavaDialog = false },
                    title = { Text("Odjava") },
                    text = { Text("Da li ste sigurni da se želite odjaviti?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showOdjavaDialog = false
                                onOdjava()
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
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    broj: String,
    label: String
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = broj,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}