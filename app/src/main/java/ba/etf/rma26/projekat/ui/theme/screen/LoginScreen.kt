package ba.etf.rma26.projekat.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    isLoading: Boolean,
    greska: String?,
    onLogin: (String) -> Unit
) {
    var hash by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Kviz aplikacija",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Unesite vaš pristupni hash za nastavak",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = hash,
                onValueChange = { hash = it },
                label = { Text("Hash") },
                singleLine = true,
                isError = greska != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hashInput")
            )

            greska?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onLogin(hash.trim()) },
                enabled = hash.isNotBlank() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("loginDugme")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Prijavi se")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Demo hash: demo",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}