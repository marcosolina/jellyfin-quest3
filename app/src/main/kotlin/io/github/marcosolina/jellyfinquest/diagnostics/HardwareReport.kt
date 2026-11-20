package io.github.marcosolina.jellyfinquest.diagnostics

enum class Availability { HARDWARE, SOFTWARE, NONE }

/** How well the headset handles one [codec], from its best decoders: hardware ones when there are any. */
data class CodecSupport(
    val codec: Codec,
    val availability: Availability,
    val maxWidth: Int? = null,
    val maxHeight: Int? = null,
    val tenBit: Boolean = false,
    val hdr: Boolean = false,
    val eightKAt60: Boolean = false,
) {
    /** Largest width and height, when the decoders report them (video only). */
    val maxSize: Pair<Int, Int>?
        get() = if (maxWidth != null && maxHeight != null) maxWidth to maxHeight else null
}

/** The headset's decoding abilities for every [Codec], in a form for the panel and for the log. */
data class HardwareReport(
    val codecs: List<CodecSupport>,
    val displayHdr: Boolean,
) {
    fun lines(): List<String> = listOf("display HDR: ${yesNo(displayHdr)}") + codecs.map(::describe)

    companion object {
        fun of(
            decoders: List<DecoderInfo>,
            displayHdr: Boolean,
        ) = HardwareReport(Codec.entries.map { support(it, decoders) }, displayHdr)

        fun support(
            codec: Codec,
            decoders: List<DecoderInfo>,
        ): CodecSupport {
            val matching = decoders.filter { it.mimeType.lowercase() in codec.mimeTypes }
            val hardware = matching.filter { it.isHardware }
            val best = hardware.ifEmpty { matching }
            val availability =
                when {
                    hardware.isNotEmpty() -> Availability.HARDWARE
                    matching.isNotEmpty() -> Availability.SOFTWARE
                    else -> Availability.NONE
                }
            return CodecSupport(
                codec = codec,
                availability = availability,
                maxWidth = best.mapNotNull { it.maxWidth }.maxOrNull(),
                maxHeight = best.mapNotNull { it.maxHeight }.maxOrNull(),
                tenBit = best.any { it.tenBit },
                hdr = best.any { it.hdr },
                eightKAt60 = best.any { it.eightKAt60 },
            )
        }

        private fun describe(support: CodecSupport): String {
            val size = support.maxSize?.let { (width, height) -> ", max ${width}x$height" }.orEmpty()
            val video =
                if (support.codec.kind == MediaKind.VIDEO && support.availability != Availability.NONE) {
                    ", 10 bit ${yesNo(support.tenBit)}, HDR ${yesNo(support.hdr)}, 8K60 ${yesNo(support.eightKAt60)}"
                } else {
                    ""
                }
            return "${support.codec.label}: ${support.availability.name.lowercase()}$size$video"
        }

        private fun yesNo(value: Boolean) = if (value) "yes" else "no"
    }
}
