package io.github.marcosolina.jellyfinquest.diagnostics

/** What one decoder of the headset says it can do for one format. Sizes are null for audio decoders. */
data class DecoderInfo(
    val name: String,
    val mimeType: String,
    val isHardware: Boolean,
    val maxWidth: Int? = null,
    val maxHeight: Int? = null,
    val tenBit: Boolean = false,
    val hdr: Boolean = false,
    val eightKAt60: Boolean = false,
)
