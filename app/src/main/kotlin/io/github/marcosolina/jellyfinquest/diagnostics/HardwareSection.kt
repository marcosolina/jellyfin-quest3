package io.github.marcosolina.jellyfinquest.diagnostics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.marcosolina.jellyfinquest.R

/** The decoder list of the milestone 1 panel (JQ-022). */
@Composable
fun HardwareSection(report: HardwareReport?) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(R.string.decoders_title), style = MaterialTheme.typography.titleLarge)
        if (report == null) {
            Text(stringResource(R.string.decoders_loading))
            return@Column
        }
        Text(stringResource(R.string.display_hdr, yesNo(report.displayHdr)))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(report.codecs, key = { it.codec }) { CodecRow(it) }
        }
    }
}

@Composable
private fun CodecRow(support: CodecSupport) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(support.codec.label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Text(availability(support.availability), modifier = Modifier.weight(1f))
        Text(features(support), modifier = Modifier.weight(2f), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun availability(value: Availability): String =
    stringResource(
        when (value) {
            Availability.HARDWARE -> R.string.availability_hardware
            Availability.SOFTWARE -> R.string.availability_software
            Availability.NONE -> R.string.availability_none
        },
    )

@Composable
private fun features(support: CodecSupport): String {
    if (support.codec.kind != MediaKind.VIDEO || support.availability == Availability.NONE) return ""
    val size = support.maxSize?.let { (width, height) -> stringResource(R.string.feature_max_size, width, height) }
    return listOfNotNull(
        size,
        stringResource(R.string.feature_ten_bit).takeIf { support.tenBit },
        stringResource(R.string.feature_hdr).takeIf { support.hdr },
        stringResource(R.string.feature_8k60).takeIf { support.eightKAt60 },
    ).joinToString(" · ")
}

@Composable
private fun yesNo(value: Boolean): String = stringResource(if (value) R.string.yes else R.string.no)
