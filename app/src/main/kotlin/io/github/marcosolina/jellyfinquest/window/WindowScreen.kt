package io.github.marcosolina.jellyfinquest.window

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.marcosolina.jellyfinquest.R
import io.github.marcosolina.jellyfinquest.diagnostics.HardwareReport
import io.github.marcosolina.jellyfinquest.diagnostics.HardwareSection
import io.github.marcosolina.jellyfinquest.ui.AppTheme

/** The window's content for milestone 2: greeting, version, the way into the cinema, and the decoder list. */
@Composable
fun WindowScreen(
    version: String,
    hardware: HardwareReport?,
    onEnterCinema: () -> Unit,
) {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
            Column(
                modifier = Modifier.padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineLarge)
                Text(stringResource(R.string.home_greeting), style = MaterialTheme.typography.titleMedium)
                Text(
                    stringResource(R.string.home_version, version),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Button(onClick = onEnterCinema) { Text(stringResource(R.string.enter_cinema)) }
                HorizontalDivider()
                HardwareSection(hardware)
            }
        }
    }
}
