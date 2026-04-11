package ba.etfrma.kvizapp.ui.theme.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ba.etfrma.kvizapp.viewmodel.FilterOpcija

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(
    odabranaOpcija: FilterOpcija,
    onOdabir: (FilterOpcija) -> Unit
) {
    var prosiren by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = prosiren,
        onExpandedChange = { prosiren = !prosiren },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .testTag("filterKvizova")
    ) {
        OutlinedTextField(
            value = odabranaOpcija.label,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = prosiren) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = prosiren,
            onDismissRequest = { prosiren = false }
        ) {
            FilterOpcija.entries.forEach { opcija ->
                DropdownMenuItem(
                    text = { Text(opcija.label) },
                    onClick = {
                        onOdabir(opcija)
                        prosiren = false
                    }
                )
            }
        }
    }
}