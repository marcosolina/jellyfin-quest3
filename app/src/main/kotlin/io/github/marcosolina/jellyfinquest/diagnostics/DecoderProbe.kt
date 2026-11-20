package io.github.marcosolina.jellyfinquest.diagnostics

import android.media.MediaCodecInfo
import android.media.MediaCodecInfo.CodecProfileLevel
import android.media.MediaCodecList
import android.media.MediaFormat

/** Asks Android which decoders the headset has and what each one supports. */
object DecoderProbe {
    // Meta's stated hardware limit for 180 and 360 video (docs/research/JQ-018).
    private const val EIGHT_K_WIDTH = 8192
    private const val EIGHT_K_HEIGHT = 4096
    private const val EIGHT_K_FPS = 60.0

    private val tenBitProfiles: Map<String, Set<Int>> =
        mapOf(
            MediaFormat.MIMETYPE_VIDEO_AVC to setOf(CodecProfileLevel.AVCProfileHigh10),
            MediaFormat.MIMETYPE_VIDEO_HEVC to
                setOf(
                    CodecProfileLevel.HEVCProfileMain10,
                    CodecProfileLevel.HEVCProfileMain10HDR10,
                    CodecProfileLevel.HEVCProfileMain10HDR10Plus,
                ),
            MediaFormat.MIMETYPE_VIDEO_VP9 to
                setOf(
                    CodecProfileLevel.VP9Profile2,
                    CodecProfileLevel.VP9Profile3,
                    CodecProfileLevel.VP9Profile2HDR,
                    CodecProfileLevel.VP9Profile3HDR,
                    CodecProfileLevel.VP9Profile2HDR10Plus,
                    CodecProfileLevel.VP9Profile3HDR10Plus,
                ),
            MediaFormat.MIMETYPE_VIDEO_AV1 to
                setOf(
                    CodecProfileLevel.AV1ProfileMain10,
                    CodecProfileLevel.AV1ProfileMain10HDR10,
                    CodecProfileLevel.AV1ProfileMain10HDR10Plus,
                ),
        )

    private val hdrProfiles: Map<String, Set<Int>> =
        mapOf(
            MediaFormat.MIMETYPE_VIDEO_HEVC to
                setOf(CodecProfileLevel.HEVCProfileMain10HDR10, CodecProfileLevel.HEVCProfileMain10HDR10Plus),
            MediaFormat.MIMETYPE_VIDEO_VP9 to
                setOf(
                    CodecProfileLevel.VP9Profile2HDR,
                    CodecProfileLevel.VP9Profile3HDR,
                    CodecProfileLevel.VP9Profile2HDR10Plus,
                    CodecProfileLevel.VP9Profile3HDR10Plus,
                ),
            MediaFormat.MIMETYPE_VIDEO_AV1 to
                setOf(CodecProfileLevel.AV1ProfileMain10HDR10, CodecProfileLevel.AV1ProfileMain10HDR10Plus),
        )

    fun probe(): List<DecoderInfo> =
        MediaCodecList(MediaCodecList.ALL_CODECS)
            .codecInfos
            .filterNot { it.isEncoder }
            .flatMap { info -> info.supportedTypes.mapNotNull { type -> describe(info, type) } }

    /** Null when the decoder cannot describe the format, which some vendor decoders do for exotic types. */
    private fun describe(
        info: MediaCodecInfo,
        mimeType: String,
    ): DecoderInfo? {
        val capabilities = runCatching { info.getCapabilitiesForType(mimeType) }.getOrNull() ?: return null
        val video = capabilities.videoCapabilities
        val profiles = capabilities.profileLevels.map { it.profile }.toSet()
        val type = mimeType.lowercase()
        return DecoderInfo(
            name = info.name,
            mimeType = mimeType,
            isHardware = info.isHardwareAccelerated,
            maxWidth = video?.supportedWidths?.upper,
            maxHeight = video?.supportedHeights?.upper,
            tenBit = profiles.any { it in tenBitProfiles[type].orEmpty() },
            hdr = type == MediaFormat.MIMETYPE_VIDEO_DOLBY_VISION || profiles.any { it in hdrProfiles[type].orEmpty() },
            eightKAt60 = video?.areSizeAndRateSupported(EIGHT_K_WIDTH, EIGHT_K_HEIGHT, EIGHT_K_FPS) == true,
        )
    }
}
