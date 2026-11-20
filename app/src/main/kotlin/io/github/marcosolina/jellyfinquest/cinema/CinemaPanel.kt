package io.github.marcosolina.jellyfinquest.cinema

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.marcosolina.jellyfinquest.R
import io.github.marcosolina.jellyfinquest.ui.AppTheme

/** The cinema's control panel for milestone 2: show or hide the room, and go back to the window. */
@Composable
fun CinemaPanel(
    passthroughOn: Boolean,
    onTogglePassthrough: () -> Unit,
    onBackToWindow: () -> Unit,
) {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(stringResource(R.string.cinema_title), style = MaterialTheme.typography.headlineMedium)
                Text(
                    stringResource(R.string.cinema_description),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = onTogglePassthrough) {
                        Text(stringResource(if (passthroughOn) R.string.room_hide else R.string.room_show))
                    }
                    OutlinedButton(onClick = onBackToWindow) { Text(stringResource(R.string.back_to_window)) }
                }
            }
        }
    }
}
