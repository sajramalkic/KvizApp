package ba.etfrma.kvizapp.ui.theme.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ba.etfrma.kvizapp.model.Grupa
import ba.etfrma.kvizapp.model.Predmet
import androidx.compose.runtime.saveable.rememberSaveable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpisForm(

    odabranaGodina: Int?,
    odabraniPredmet: Predmet?,
    odabranaGrupa: Grupa?,
    predmetiZaGodinu: List<Predmet>,
    grupeZaPredmet: List<Grupa>,
    dugmeEnabled: Boolean,
    onGodinaOdabrana: (Int) -> Unit,
    onPredmetOdabran: (Predmet) -> Unit,
    onGrupaOdabrana: (Grupa) -> Unit,
    onUpis: () -> Unit
) {
    var posljednjaGodina by rememberSaveable { mutableStateOf<Int?>(null) }

    LaunchedEffect(odabranaGodina) {
        if (odabranaGodina != null) posljednjaGodina = odabranaGodina
    }

    val prikazanaGodina = odabranaGodina ?: posljednjaGodina

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Upis na predmet",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Odabir godine
        var godinaProsirenа by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = godinaProsirenа,
            onExpandedChange = { godinaProsirenа = !godinaProsirenа },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = odabranaGodina?.toString() ?: "Odaberi godinu",
                onValueChange = {},
                readOnly = true,
                label = { Text("Godina studija") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = godinaProsirenа) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag("odabirGodina")
            )
            ExposedDropdownMenu(
                expanded = godinaProsirenа,
                onDismissRequest = { godinaProsirenа = false }
            ) {
                (1..5).forEach { godina ->
                    DropdownMenuItem(
                        text = { Text("$godina. godina") },
                        onClick = {
                            onGodinaOdabrana(godina)
                            godinaProsirenа = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Odabir predmeta
        var predmetProsiren by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = predmetProsiren,
            onExpandedChange = {
                if (odabranaGodina != null) predmetProsiren = !predmetProsiren
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = odabraniPredmet?.naziv ?: "Odaberi predmet",
                onValueChange = {},
                readOnly = true,
                label = { Text("Predmet") },
                enabled = odabranaGodina != null,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = predmetProsiren) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag("odabirPredmet")
            )
            ExposedDropdownMenu(
                expanded = predmetProsiren,
                onDismissRequest = { predmetProsiren = false }
            ) {
                if (predmetiZaGodinu.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Nema dostupnih predmeta") },
                        onClick = { predmetProsiren = false }
                    )
                } else {
                    predmetiZaGodinu.forEach { predmet ->
                        DropdownMenuItem(
                            text = { Text(predmet.naziv) },
                            onClick = {
                                onPredmetOdabran(predmet)
                                predmetProsiren = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Odabir grupe
        var grupaProsirena by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = grupaProsirena,
            onExpandedChange = {
                if (odabraniPredmet != null) grupaProsirena = !grupaProsirena
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = odabranaGrupa?.naziv ?: "Odaberi grupu",
                onValueChange = {},
                readOnly = true,
                label = { Text("Grupa") },
                enabled = odabraniPredmet != null,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = grupaProsirena) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag("odabirGrupa")
            )
            ExposedDropdownMenu(
                expanded = grupaProsirena,
                onDismissRequest = { grupaProsirena = false }
            ) {
                grupeZaPredmet.forEach { grupa ->
                    DropdownMenuItem(
                        text = { Text(grupa.naziv) },
                        onClick = {
                            onGrupaOdabrana(grupa)
                            grupaProsirena = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onUpis,
            enabled = dugmeEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("dodajPredmetDugme")
        ) {
            Text("Upiši me")
        }
    }
}